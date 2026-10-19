package com.pawcare.dao;
import com.pawcare.model.Payment;
import com.pawcare.exception.InvalidPaymentException;
import java.sql.*;
import java.util.*;
public class PaymentDAO extends DaoSupport {
    public List<Payment> findAll(String search) throws SQLException {
        List<Payment> rows=new ArrayList<>();
        String sql="SELECT p.*,c.name customer_name,s.service_name FROM payments p JOIN appointments a ON p.appointment_id=a.appointment_id JOIN customers c ON a.customer_id=c.customer_id JOIN services s ON a.service_id=s.service_id WHERE c.name LIKE ? OR s.service_name LIKE ? OR p.payment_method LIKE ? ORDER BY p.payment_id DESC";
        try(Connection c=open(); PreparedStatement s=c.prepareStatement(sql)) {
            for(int i=1;i<=3;i++) s.setString(i,"%"+search.trim()+"%");
            try(ResultSet r=s.executeQuery()) { while(r.next()) {
                Payment p=new Payment(); p.setId(r.getInt("payment_id")); p.setAppointmentId(r.getInt("appointment_id")); p.setAmount(r.getBigDecimal("amount")); p.setMethod(r.getString("payment_method")); p.setDate(r.getDate("payment_date").toLocalDate()); p.setCustomerName(r.getString("customer_name")); p.setServiceName(r.getString("service_name")); rows.add(p);
            } }
        } return rows;
    }
    public void save(Payment p) throws SQLException, InvalidPaymentException {
        try(Connection c=open()) { c.setAutoCommit(false);
            try { lockCalendar(c);
                if(p.getId()!=0) {
                    try(PreparedStatement s=c.prepareStatement("SELECT appointment_id FROM payments WHERE payment_id=?")) {
                        s.setInt(1,p.getId()); try(ResultSet r=s.executeQuery()) {
                            if(!r.next()) throw new InvalidPaymentException("This payment no longer exists.");
                            if(r.getInt(1)!=p.getAppointmentId()) throw new InvalidPaymentException("An existing payment cannot be moved to another appointment.");
                        }
                    }
                }
                try(PreparedStatement s=c.prepareStatement("SELECT booked_price,status FROM appointments WHERE appointment_id=?")) {
                    s.setInt(1,p.getAppointmentId()); try(ResultSet r=s.executeQuery()) {
                        if(!r.next()) throw new InvalidPaymentException("Select an existing appointment.");
                        if("Cancelled".equals(r.getString("status"))) throw new InvalidPaymentException("A cancelled appointment cannot be paid.");
                        if(p.getAmount().compareTo(r.getBigDecimal("booked_price"))!=0) throw new InvalidPaymentException("Amount must match the appointment's booked price. This system records one full payment.");
                    }
                }
                try(PreparedStatement s=c.prepareStatement("SELECT payment_id FROM payments WHERE appointment_id=? AND payment_id<>?")) {
                    s.setInt(1,p.getAppointmentId()); s.setInt(2,p.getId()); try(ResultSet r=s.executeQuery()) { if(r.next()) throw new InvalidPaymentException("This appointment already has a payment. Select that payment to update it."); }
                }
                String sql=p.getId()==0?"INSERT INTO payments(appointment_id,amount,payment_method,payment_date) VALUES (?,?,?,?)":"UPDATE payments SET appointment_id=?,amount=?,payment_method=?,payment_date=? WHERE payment_id=?";
                try(PreparedStatement s=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
                    s.setInt(1,p.getAppointmentId()); s.setBigDecimal(2,p.getAmount()); s.setString(3,p.getMethod()); s.setDate(4,java.sql.Date.valueOf(p.getDate()));
                    if(p.getId()!=0) s.setInt(5,p.getId()); changed(s.executeUpdate()); if(p.getId()==0) p.setId(key(s));
                } c.commit();
            } catch(SQLException | InvalidPaymentException | RuntimeException e) { c.rollback(); throw e; }
        }
    }
    public void delete(int id) throws SQLException {
        try(Connection c=open()) { c.setAutoCommit(false);
            try { lockCalendar(c);
                try(PreparedStatement s=c.prepareStatement("DELETE FROM payments WHERE payment_id=?")) { s.setInt(1,id); changed(s.executeUpdate()); }
                c.commit();
            } catch(SQLException e) { c.rollback(); throw e; }
        }
    }
}
