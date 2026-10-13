package com.pawcare.controller;
import com.pawcare.dao.PetDAO;
import com.pawcare.model.Pet;
import com.pawcare.service.Validation;
import com.pawcare.exception.ValidationException;
import java.sql.SQLException;
import java.util.List;
public class PetController {
    private final PetDAO dao=new PetDAO();
    public List<Pet> list(String search) throws SQLException { return dao.findAll(search); }
    public void save(Pet v) throws SQLException, ValidationException {
        validate(v);
        if(v.getId()==0) v.setId(dao.insert(v)); else dao.update(v);
    }
    public void delete(int id) throws SQLException, ValidationException {
        if(id<=0) throw new ValidationException("Select a row first.");
        dao.delete(id);
    }
    public void validate(Pet v) throws ValidationException {
        if(v.getCustomerId()<=0) throw new ValidationException("Select a registered customer.");
        v.setPetName(Validation.text(v.getPetName(),"Pet name",80));
        v.setBreed(Validation.text(v.getBreed(),"Breed",80));
        Validation.choice(v.getAnimalType(),"animal type","Dog","Cat","Rabbit","Other");
        Validation.choice(v.getGender(),"gender","Male","Female");
        if(v.getAge()<1 || v.getAge()>100) throw new ValidationException("Age must be a whole number from 1 to 100 years.");
    }
}
