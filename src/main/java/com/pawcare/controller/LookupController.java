package com.pawcare.controller;
import com.pawcare.dao.*;
import com.pawcare.model.*;
import java.sql.SQLException;
import java.util.List;
/** Reference data for form dropdowns. Views never run SQL themselves. */
public class LookupController {
    public List<Customer> customers() throws SQLException { return new CustomerDAO().findAll(""); }
    public List<Pet> pets() throws SQLException { return new PetDAO().findAll(""); }
    public List<GroomingService> services() throws SQLException { return new ServiceDAO().findAll(""); }
    public List<Appointment> appointments() throws SQLException { return new AppointmentDAO().findAll(""); }
}
