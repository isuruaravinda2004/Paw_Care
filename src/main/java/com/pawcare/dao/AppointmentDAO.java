package com.pawcare.dao;
import com.pawcare.model.Appointment;
import com.pawcare.exception.InvalidAppointmentException;
import com.pawcare.util.AppClock;
import java.sql.*;
import java.time.*;
import java.util.*;

public class AppointmentDAO extends DaoSupport {
    private static final String SELECT="SELECT a.*,c.name customer_name,p.pet_name,s.service_name FROM appointments a JOIN customers c ON a.customer_id=c.customer_id JOIN pets p ON a.pet_id=p.pet_id JOIN services s ON a.service_id=s.service_id";
    public List<Appointment> findAll(String search) throws SQLException {
        List<Appointment> rows=new ArrayList<>();
        try(Connection c=open(); PreparedStatement s=c.prepareStatement(SELECT+" WHERE c.name LIKE ? OR p.pet_name LIKE ? OR s.service_name LIKE ? OR a.status LIKE ? ORDER BY a.appointment_date DESC,a.appointment_time DESC")) {
            for(int i=1;i<=4;i++) s.setString(i,"%"+search.trim()+"%");
            try(ResultSet r=s.executeQuery()) { while(r.next()) rows.add(map(r)); }
        }
        return rows;
    }
    public Appointment findById(int id) throws SQLException { try(Connection c=open()) { return find(c,id); } }
    private Appointment find(Connection c,int id) throws SQLException {
        try(PreparedStatement s=c.prepareStatement(SELECT+" WHERE a.appointment_id=?")) {
            s.setInt(1,id); try(ResultSet r=s.executeQuery()) { return r.next()?map(r):null; }
        }
    }
    public void save(Appointment a) throws SQLException, InvalidAppointmentException {
        try(Connection c=open()) {
            c.setAutoCommit(false);
            try {
                lockCalendar(c);
                Appointment old=a.getId()==0?null:find(c,a.getId());
                if(a.getId()!=0 && old==null) throw new InvalidAppointmentException("This appointment was deleted. Refresh the list.");
                boolean scheduleChanged=old==null || !old.getDate().equals(a.getDate()) || !old.getTime().equals(a.getTime()) || old.getServiceId()!=a.getServiceId() || old.getPetId()!=a.getPetId();
                if(scheduleChanged && a.getDate().atTime(a.getTime()).isBefore(AppClock.now()))
                    throw new InvalidAppointmentException("Book or reschedule appointments for a future date and time.");
                if("Completed".equals(a.getStatus()) && a.getDate().atTime(a.getTime()).isAfter(AppClock.now()))
                    throw new InvalidAppointmentException("A future appointment cannot be marked completed.");
                if(old!=null && hasPayment(c,a.getId()) && (scheduleChanged || a.getCustomerId()!=old.getCustomerId() || "Cancelled".equals(a.getStatus())))
                    throw new InvalidAppointmentException("This appointment is paid. Remove the payment before rescheduling or cancelling.");
                try(PreparedStatement s=c.prepareStatement("SELECT pet_id FROM pets WHERE pet_id=? AND customer_id=?")) {
                    s.setInt(1,a.getPetId()); s.setInt(2,a.getCustomerId());
                    try(ResultSet r=s.executeQuery()) { if(!r.next()) throw new InvalidAppointmentException("The selected pet does not belong to this customer."); }
                }
                try(PreparedStatement s=c.prepareStatement("SELECT price,duration_minutes FROM services WHERE service_id=?")) {
                    s.setInt(1,a.getServiceId());
                    try(ResultSet r=s.executeQuery()) {
                        if(!r.next()) throw new InvalidAppointmentException("Select an existing grooming service.");
                        // Keep a historical price/duration until a different service is chosen.
                        a.setBookedPrice(old!=null && old.getServiceId()==a.getServiceId()?old.getBookedPrice():r.getBigDecimal("price"));
                        a.setDurationMinutes(old!=null && old.getServiceId()==a.getServiceId()?old.getDurationMinutes():r.getInt("duration_minutes"));
                    }
                }
                int start=a.getTime().getHour()*60+a.getTime().getMinute();
                if(start+a.getDurationMinutes()>1440) throw new InvalidAppointmentException("The service must finish before midnight.");
                if(!"Cancelled".equals(a.getStatus())) {
                    try(PreparedStatement s=c.prepareStatement("SELECT appointment_time,duration_minutes FROM appointments WHERE appointment_date=? AND status<>'Cancelled' AND appointment_id<>?")) {
                        s.setDate(1,java.sql.Date.valueOf(a.getDate())); s.setInt(2,a.getId());
                        try(ResultSet r=s.executeQuery()) {
                            while(r.next()) {
                                LocalTime time=r.getTime(1).toLocalTime(); int other=time.getHour()*60+time.getMinute();
                                if(start<other+r.getInt(2) && start+a.getDurationMinutes()>other)
                                    throw new InvalidAppointmentException("This time overlaps another appointment. Choose a free time slot.");
                            }
                        }
                    }
                }
                String sql=a.getId()==0?"INSERT INTO appointments(customer_id,pet_id,service_id,appointment_date,appointment_time,status,booked_price,duration_minutes) VALUES (?,?,?,?,?,?,?,?)":"UPDATE appointments SET customer_id=?,pet_id=?,service_id=?,appointment_date=?,appointment_time=?,status=?,booked_price=?,duration_minutes=? WHERE appointment_id=?";
                try(PreparedStatement s=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
                    s.setInt(1,a.getCustomerId()); s.setInt(2,a.getPetId()); s.setInt(3,a.getServiceId());
                    s.setDate(4,java.sql.Date.valueOf(a.getDate())); s.setTime(5,Time.valueOf(a.getTime())); s.setString(6,a.getStatus()); s.setBigDecimal(7,a.getBookedPrice()); s.setInt(8,a.getDurationMinutes());
                    if(a.getId()!=0) s.setInt(9,a.getId()); changed(s.executeUpdate());
                    if(a.getId()==0) a.setId(key(s));
                }
                c.commit();
            } catch(SQLException | InvalidAppointmentException | RuntimeException ex) { c.rollback(); throw ex; }
        }
    }
    private boolean hasPayment(Connection c,int id) throws SQLException {
        try(PreparedStatement s=c.prepareStatement("SELECT payment_id FROM payments WHERE appointment_id=?")) {
            s.setInt(1,id); try(ResultSet r=s.executeQuery()) { return r.next(); }
        }
    }
    public void cancel(int id) throws SQLException, InvalidAppointmentException {
        try(Connection c=open()) { c.setAutoCommit(false);
            try { lockCalendar(c);
                if(hasPayment(c,id)) throw new InvalidAppointmentException("Remove the payment before cancelling this appointment.");
                try(PreparedStatement s=c.prepareStatement("UPDATE appointments SET status='Cancelled' WHERE appointment_id=?")) { s.setInt(1,id); changed(s.executeUpdate()); }
                c.commit();
            } catch(SQLException | InvalidAppointmentException ex) { c.rollback(); throw ex; }
        }
    }
    public void delete(int id) throws SQLException, InvalidAppointmentException {
        try(Connection c=open()) { c.setAutoCommit(false);
            try { lockCalendar(c);
                if(hasPayment(c,id)) throw new InvalidAppointmentException("Remove the payment before deleting this appointment.");
                try(PreparedStatement s=c.prepareStatement("DELETE FROM appointments WHERE appointment_id=?")) { s.setInt(1,id); changed(s.executeUpdate()); }
                c.commit();
            } catch(SQLException | InvalidAppointmentException ex) { c.rollback(); throw ex; }
        }
    }
    private Appointment map(ResultSet r) throws SQLException {
        Appointment a=new Appointment(); a.setId(r.getInt("appointment_id")); a.setCustomerId(r.getInt("customer_id")); a.setPetId(r.getInt("pet_id")); a.setServiceId(r.getInt("service_id"));
        a.setDate(r.getDate("appointment_date").toLocalDate()); a.setTime(r.getTime("appointment_time").toLocalTime()); a.setStatus(r.getString("status")); a.setBookedPrice(r.getBigDecimal("booked_price")); a.setDurationMinutes(r.getInt("duration_minutes"));
        a.setCustomerName(r.getString("customer_name")); a.setPetName(r.getString("pet_name")); a.setServiceName(r.getString("service_name")); return a;
    }
}
