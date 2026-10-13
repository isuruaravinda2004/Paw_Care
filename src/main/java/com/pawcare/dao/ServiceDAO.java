package com.pawcare.dao;
import java.sql.*;
import java.util.*;
import com.pawcare.model.GroomingService;
/** Prepared statements prevent input from becoming executable SQL. */
public class ServiceDAO extends DaoSupport {
    public List<GroomingService> findAll(String search) throws SQLException {
        List<GroomingService> rows=new ArrayList<>();
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("SELECT s.* FROM services s WHERE s.service_name LIKE ? OR s.description LIKE ? ORDER BY s.service_id DESC")) {
            for(int i=1;i<=2;i++) s.setString(i,"%"+search.trim()+"%");
            try(ResultSet r=s.executeQuery()) { while(r.next()) rows.add(map(r)); }
        }
        return rows;
    }
    public GroomingService findById(int id) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("SELECT s.* FROM services s WHERE s.service_id=?")) {
            s.setInt(1,id); try(ResultSet r=s.executeQuery()) { return r.next()?map(r):null; }
        }
    }
    public int insert(GroomingService v) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("INSERT INTO services(service_name,description,price,duration_minutes) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            bind(s,v); changed(s.executeUpdate()); return key(s);
        }
    }
    public void update(GroomingService v) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("UPDATE services SET service_name=?,description=?,price=?,duration_minutes=? WHERE service_id=?")) {
            bind(s,v); s.setInt(5,v.getId()); changed(s.executeUpdate());
        }
    }
    public void delete(int id) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("DELETE FROM services WHERE service_id=?")) {
            s.setInt(1,id); changed(s.executeUpdate());
        }
    }
    private void bind(PreparedStatement s,GroomingService v) throws SQLException {
        s.setString(1,v.getServiceName());
        s.setString(2,v.getDescription());
        s.setBigDecimal(3,v.getPrice());
        s.setInt(4,v.getDurationMinutes());
    }
    private GroomingService map(ResultSet r) throws SQLException {
        GroomingService v=new GroomingService(); v.setId(r.getInt("service_id"));
        v.setServiceName(r.getString("service_name"));
        v.setDescription(r.getString("description"));
        v.setPrice(r.getBigDecimal("price"));
        v.setDurationMinutes(r.getInt("duration_minutes"));
         return v;
    }
}
