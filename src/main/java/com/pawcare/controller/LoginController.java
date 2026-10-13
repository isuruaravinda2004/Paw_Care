package com.pawcare.controller;
import com.pawcare.dao.UserDAO;
import com.pawcare.model.User;
import com.pawcare.exception.ValidationException;
import com.pawcare.util.PasswordUtil;
import java.sql.SQLException;
public class LoginController {
    private final UserDAO dao = new UserDAO();
    public User login(String username, char[] password) throws SQLException, ValidationException {
        if(username == null || username.trim().isEmpty() || password.length==0)
            throw new ValidationException("Enter your username and password.");
        User user=dao.authenticate(username.trim(),password);
        if(user==null) throw new ValidationException("Incorrect username or password.");
        return user;
    }
    public void changePassword(User user, char[] current, char[] next) throws SQLException, ValidationException {
        if(dao.authenticate(user.getUsername(),current)==null) throw new ValidationException("Your current password is incorrect.");
        if(next.length<8) throw new ValidationException("Use at least 8 characters for the new password.");
        dao.changePassword(user.getId(),PasswordUtil.hash(next));
    }
}
