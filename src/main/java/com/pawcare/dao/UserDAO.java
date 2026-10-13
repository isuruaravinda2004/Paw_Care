package com.pawcare.dao;
import java.sql.*;
import com.pawcare.model.User;
import com.pawcare.util.PasswordUtil;
public class UserDAO extends DaoSupport {
    public User authenticate(String username, char[] password) throws SQLException {
        try (Connection c=open(); PreparedStatement s=c.prepareStatement("SELECT * FROM users WHERE username=?")) {
            s.setString(1,username);
            try(ResultSet r=s.executeQuery()) {
                if (r.next() && PasswordUtil.verify(password,r.getString("password_hash"))) {
                    User u=new User(); u.setId(r.getInt("user_id")); u.setUsername(r.getString("username")); u.setRole(r.getString("role")); return u;
                }
            }
        }
        return null;
    }
    public void changePassword(int userId, String hash) throws SQLException {
        try(Connection c=open(); PreparedStatement s=c.prepareStatement("UPDATE users SET password_hash=? WHERE user_id=?")) {
            s.setString(1,hash); s.setInt(2,userId); changed(s.executeUpdate());
        }
    }
}
