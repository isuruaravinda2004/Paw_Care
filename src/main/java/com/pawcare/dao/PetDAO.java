package com.pawcare.dao;
import java.sql.*;
import java.util.*;
import com.pawcare.model.Pet;
/** Prepared statements prevent input from becoming executable SQL. */
public class PetDAO extends DaoSupport {
    public List<Pet> findAll(String search) throws SQLException {
        List<Pet> rows=new ArrayList<>();
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("SELECT p.*, c.name AS owner_name FROM pets p JOIN customers c ON p.customer_id=c.customer_id WHERE p.pet_name LIKE ? OR c.name LIKE ? OR p.breed LIKE ? ORDER BY p.pet_id DESC")) {
            for(int i=1;i<=3;i++) s.setString(i,"%"+search.trim()+"%");
            try(ResultSet r=s.executeQuery()) { while(r.next()) rows.add(map(r)); }
        }
        return rows;
    }
    public Pet findById(int id) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("SELECT p.*, c.name AS owner_name FROM pets p JOIN customers c ON p.customer_id=c.customer_id WHERE p.pet_id=?")) {
            s.setInt(1,id); try(ResultSet r=s.executeQuery()) { return r.next()?map(r):null; }
        }
    }
    public int insert(Pet v) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("INSERT INTO pets(customer_id,pet_name,animal_type,breed,gender,age) VALUES (?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            bind(s,v); changed(s.executeUpdate()); return key(s);
        }
    }
    public void update(Pet v) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("UPDATE pets SET customer_id=?,pet_name=?,animal_type=?,breed=?,gender=?,age=? WHERE pet_id=?")) {
            bind(s,v); s.setInt(7,v.getId()); changed(s.executeUpdate());
        }
    }
    public void delete(int id) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("DELETE FROM pets WHERE pet_id=?")) {
            s.setInt(1,id); changed(s.executeUpdate());
        }
    }
    private void bind(PreparedStatement s,Pet v) throws SQLException {
        s.setInt(1,v.getCustomerId());
        s.setString(2,v.getPetName());
        s.setString(3,v.getAnimalType());
        s.setString(4,v.getBreed());
        s.setString(5,v.getGender());
        s.setInt(6,v.getAge());
    }
    private Pet map(ResultSet r) throws SQLException {
        Pet v=new Pet(); v.setId(r.getInt("pet_id"));
        v.setCustomerId(r.getInt("customer_id"));
        v.setPetName(r.getString("pet_name"));
        v.setAnimalType(r.getString("animal_type"));
        v.setBreed(r.getString("breed"));
        v.setGender(r.getString("gender"));
        v.setAge(r.getInt("age"));
        v.setOwnerName(r.getString("owner_name")); return v;
    }
    public List<Pet> findByCustomer(int customerId) throws SQLException {
        List<Pet> result=new ArrayList<>();
        for(Pet pet:findAll("")) if(pet.getCustomerId()==customerId) result.add(pet);
        return result;
    }
}
