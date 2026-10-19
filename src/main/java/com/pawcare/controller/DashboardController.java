package com.pawcare.controller;
import com.pawcare.dao.DashboardDAO;
import java.sql.SQLException;
public class DashboardController {
    public String[] loadDashboardData() throws SQLException { return new DashboardDAO().load(); }
}
