package com.pawcare.controller;
import com.pawcare.dao.CustomerDAO;
import com.pawcare.model.Customer;
import com.pawcare.service.Validation;
import com.pawcare.exception.ValidationException;
import java.sql.SQLException;
import java.util.List;
public class CustomerController {
    private final CustomerDAO dao=new CustomerDAO();
    public List<Customer> list(String search) throws SQLException { return dao.findAll(search); }
    public void save(Customer v) throws SQLException, ValidationException {
        validate(v);
        if(v.getId()==0) v.setId(dao.insert(v)); else dao.update(v);
    }
    public void delete(int id) throws SQLException, ValidationException {
        if(id<=0) throw new ValidationException("Select a row first.");
        dao.delete(id);
    }
    public void validate(Customer v) throws ValidationException {
        v.setName(Validation.text(v.getName(),"Customer name",100));
        if(v.getPhone()==null || !v.getPhone().matches("[0-9]{10}")) throw new ValidationException("Phone must contain exactly 10 digits.");
        v.setEmail(Validation.text(v.getEmail(),"Email",150));
        if(!v.getEmail().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) throw new ValidationException("Enter a valid email address.");
        v.setAddress(Validation.text(v.getAddress(),"Address",255));
    }
}
