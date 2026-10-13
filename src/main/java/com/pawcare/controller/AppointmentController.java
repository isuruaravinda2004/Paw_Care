package com.pawcare.controller;
import com.pawcare.model.Appointment;
import com.pawcare.dao.AppointmentDAO;
import com.pawcare.exception.*;
import com.pawcare.service.Validation;
import java.sql.SQLException;
import java.util.List;
public class AppointmentController {
    private final AppointmentDAO dao=new AppointmentDAO();
    public List<Appointment> list(String search) throws SQLException { return dao.findAll(search); }
    public void save(Appointment a) throws SQLException, ValidationException {
        if(a.getCustomerId()<=0) throw new InvalidAppointmentException("Please select a customer.");
        if(a.getPetId()<=0) throw new InvalidAppointmentException("Please select a pet before booking the appointment.");
        if(a.getServiceId()<=0) throw new InvalidAppointmentException("Please select a grooming service.");
        if(a.getDate()==null || a.getTime()==null) throw new InvalidAppointmentException("Date and time are required.");
        Validation.choice(a.getStatus(),"appointment status","Pending","Confirmed","Completed","Cancelled");
        dao.save(a);
    }
    public void cancel(int id) throws SQLException, ValidationException { selected(id); dao.cancel(id); }
    public void delete(int id) throws SQLException, ValidationException { selected(id); dao.delete(id); }
    private void selected(int id) throws InvalidAppointmentException { if(id<=0) throw new InvalidAppointmentException("Select an appointment row first."); }
}
