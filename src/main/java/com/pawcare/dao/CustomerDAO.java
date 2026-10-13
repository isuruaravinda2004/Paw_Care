package com.pawcare.dao;
import java.sql.*;
import java.util.*;
import com.pawcare.model.Customer;
/** Prepared statements prevent input from becoming executable SQL. */
public class CustomerDAO extends DaoSupport {
    public List<Customer> findAll(String search) throws SQLException {
        List<Customer> rows=new ArrayList<>();
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("SELECT c.* FROM customers c WHERE c.name LIKE ? OR c.phone LIKE ? OR c.email LIKE ? ORDER BY c.customer_id DESC")) {
            for(int i=1;i<=3;i++) s.setString(i,"%"+search.trim()+"%");
            try(ResultSet r=s.executeQuery()) { while(r.next()) rows.add(map(r)); }
        }
        return rows;
    }
    public Customer findById(int id) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("SELECT c.* FROM customers c WHERE c.customer_id=?")) {
            s.setInt(1,id); try(ResultSet r=s.executeQuery()) { return r.next()?map(r):null; }
        }
    }
    public int insert(Customer v) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("INSERT INTO customers(name,phone,email,address) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            bind(s,v); changed(s.executeUpdate()); return key(s);
        }
    }
    public void update(Customer v) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("UPDATE customers SET name=?,phone=?,email=?,address=? WHERE customer_id=?")) {
            bind(s,v); s.setInt(5,v.getId()); changed(s.executeUpdate());
        }
    }
    public void delete(int id) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("DELETE FROM customers WHERE customer_id=?")) {
            s.setInt(1,id); changed(s.executeUpdate());
        }
    }
    private void bind(PreparedStatement s,Customer v) throws SQLException {
        s.setString(1,v.getName());
        s.setString(2,v.getPhone());
        s.setString(3,v.getEmail());
        s.setString(4,v.getAddress());
    }
    private Customer map(ResultSet r) throws SQLException {
        Customer v=new Customer(); v.setId(r.getInt("customer_id"));
        v.setName(r.getString("name"));
        v.setPhone(r.getString("phone"));
        v.setEmail(r.getString("email"));
        v.setAddress(r.getString("address"));
         return v;
    }
}
