package com.pawcare.controller;
import com.pawcare.dao.ServiceDAO;
import com.pawcare.model.GroomingService;
import com.pawcare.service.Validation;
import com.pawcare.exception.ValidationException;
import java.sql.SQLException;
import java.util.List;
public class ServiceController {
    private final ServiceDAO dao=new ServiceDAO();
    public List<GroomingService> list(String search) throws SQLException { return dao.findAll(search); }
    public void save(GroomingService v) throws SQLException, ValidationException {
        validate(v);
        if(v.getId()==0) v.setId(dao.insert(v)); else dao.update(v);
    }
    public void delete(int id) throws SQLException, ValidationException {
        if(id<=0) throw new ValidationException("Select a row first.");
        dao.delete(id);
    }
    public void validate(GroomingService v) throws ValidationException {
        v.setServiceName(Validation.text(v.getServiceName(),"Service name",100));
        v.setDescription(Validation.text(v.getDescription(),"Description",255));
        if(v.getPrice()==null) throw new ValidationException("Enter a price.");
        v.setPrice(Validation.money(v.getPrice().toPlainString(),"Price"));
        if(v.getDurationMinutes()<5 || v.getDurationMinutes()>480) throw new ValidationException("Duration must be 5 to 480 minutes.");
    }
}
