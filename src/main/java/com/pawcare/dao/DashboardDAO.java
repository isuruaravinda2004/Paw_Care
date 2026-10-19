package com.pawcare.dao;
import com.pawcare.util.AppClock;
import java.sql.*;
import java.math.BigDecimal;
/** All dashboard values come from MySQL; there are no static statistics. */
public class DashboardDAO extends DaoSupport {
    public String[] load() throws SQLException {
        String[] values=new String[6];
        try(Connection c=open()) {
            values[0]=count(c,"SELECT COUNT(*) FROM customers");
            values[1]=count(c,"SELECT COUNT(*) FROM pets");
            try(PreparedStatement s=c.prepareStatement("SELECT COUNT(*) FROM appointments WHERE appointment_date=?")) {
                s.setDate(1,java.sql.Date.valueOf(AppClock.today())); try(ResultSet r=s.executeQuery()) { r.next(); values[2]=r.getString(1); }
            }
            values[3]=count(c,"SELECT COUNT(*) FROM appointments WHERE status='Pending'");
            values[4]=count(c,"SELECT COUNT(*) FROM appointments WHERE status='Completed'");
            try(PreparedStatement s=c.prepareStatement("SELECT COALESCE(SUM(amount),0) FROM payments WHERE payment_date=?")) {
                s.setDate(1,java.sql.Date.valueOf(AppClock.today())); try(ResultSet r=s.executeQuery()) { r.next(); values[5]="Rs. "+r.getBigDecimal(1).setScale(2).toPlainString(); }
            }
        } return values;
    }
    private String count(Connection c,String sql) throws SQLException {
        try(PreparedStatement s=c.prepareStatement(sql); ResultSet r=s.executeQuery()) { r.next(); return r.getString(1); }
    }
}
