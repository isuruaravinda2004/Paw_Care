package com.pawcare.dao;

import java.sql.*;
import com.pawcare.util.DBConnection;

public abstract class DaoSupport {
    protected Connection open() throws SQLException { return DBConnection.getInstance().getConnection(); }
    protected void changed(int count) throws SQLException {
        if (count != 1) throw new SQLException("This record no longer exists. Refresh and try again.", "02000");
    }
    protected int key(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) return rs.getInt(1);
            throw new SQLException("The database did not return a record ID.");
        }
    }
    // The same row is locked for bookings, cancellation, payment and deletion.
    // This makes conflict detection safe across two simultaneously open applications.
    protected void lockCalendar(Connection con) throws SQLException {
        try (PreparedStatement st = con.prepareStatement("SELECT lock_name FROM system_locks WHERE lock_name='appointments' FOR UPDATE"); ResultSet rs = st.executeQuery()) {
            if (!rs.next()) throw new SQLException("Calendar lock missing. Import the complete database.sql.");
        }
    }
}
