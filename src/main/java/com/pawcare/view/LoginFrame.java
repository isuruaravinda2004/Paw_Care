package com.pawcare.view;
import com.pawcare.util.*;
import com.pawcare.model.*;
import com.pawcare.controller.*;
import com.pawcare.service.*;
import com.pawcare.exception.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.time.*;

/** JFrame and paired .form are editable in NetBeans Design view. */
public class LoginFrame extends javax.swing.JFrame {

    private final LoginController controller=new LoginController();
    public LoginFrame() {
        initComponents(); Ui.decorate(this,"Login");
        setMinimumSize(new java.awt.Dimension(780,510)); setSize(850,550); setLocationRelativeTo(null);
        txtUsername.setText("admin"); getRootPane().setDefaultButton(btnLogin);
        btnLogin.addActionListener(e -> login()); btnExit.addActionListener(e -> dispose());
    }
    private void login() {
        String username=txtUsername.getText(); char[] password=txtPassword.getPassword();
        lblStatus.setText("Signing in...");
        Tasks.run(this,btnLogin,() -> {
            try { return controller.login(username,password); } finally { Arrays.fill(password,'\0'); }
        },user -> { Session.setUser(user); new DashboardFrame().setVisible(true); dispose(); });
    }

    // Keep business logic outside the NetBeans guarded blocks.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {//GEN-BEGIN:initComponents
        pnlSidebar = new javax.swing.JPanel();
        pnlBrand = new javax.swing.JPanel();
        lblBrand = new javax.swing.JLabel();
        lblTagline = new javax.swing.JLabel();
        pnlContent = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblEyebrow = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlForm = new javax.swing.JPanel();
        pnlFieldtxtUsername = new javax.swing.JPanel();
        lblFieldUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        pnlFieldtxtPassword = new javax.swing.JPanel();
        lblFieldPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        pnlActions = new javax.swing.JPanel();
        btnLogin = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PawCare | Sign in");
        getContentPane().setLayout(new java.awt.BorderLayout());
        pnlSidebar.setName("pnlSidebar");
        pnlSidebar.setLayout(new java.awt.BorderLayout(10,10));
        pnlBrand.setName("pnlBrand");
        pnlBrand.setLayout(new java.awt.GridLayout(0,1,12,12));
        lblBrand.setText("PawCare");
        lblBrand.setName("lblBrand");
        pnlBrand.add(lblBrand);
        lblTagline.setText("Care for every pet");
        lblTagline.setName("lblTagline");
        pnlBrand.add(lblTagline);
        pnlSidebar.add(pnlBrand, java.awt.BorderLayout.NORTH);
        getContentPane().add(pnlSidebar, java.awt.BorderLayout.WEST);
        pnlContent.setName("pnlContent");
        pnlContent.setLayout(new java.awt.BorderLayout(10,10));
        pnlHeader.setName("pnlHeader");
        pnlHeader.setLayout(new java.awt.GridLayout(0,1,12,12));
        lblEyebrow.setText("PAWCARE / GROOMING CENTER");
        lblEyebrow.setName("lblEyebrow");
        pnlHeader.add(lblEyebrow);
        lblTitle.setText("Welcome to PawCare");
        lblTitle.setName("lblTitle");
        pnlHeader.add(lblTitle);
        lblSubtitle.setText("Sign in to manage your grooming center.");
        lblSubtitle.setName("lblSubtitle");
        pnlHeader.add(lblSubtitle);
        pnlContent.add(pnlHeader, java.awt.BorderLayout.NORTH);
        pnlForm.setName("pnlForm");
        pnlForm.setLayout(new java.awt.GridLayout(0,1,12,12));
        pnlFieldtxtUsername.setName("pnlFieldtxtUsername");
        pnlFieldtxtUsername.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldUsername.setText("Username");
        lblFieldUsername.setName("lblFieldUsername");
        pnlFieldtxtUsername.add(lblFieldUsername, java.awt.BorderLayout.NORTH);
        txtUsername.setColumns(18);
        txtUsername.setEditable(true);
        txtUsername.setName("txtUsername");
        pnlFieldtxtUsername.add(txtUsername, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtUsername);
        pnlFieldtxtPassword.setName("pnlFieldtxtPassword");
        pnlFieldtxtPassword.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldPassword.setText("Password");
        lblFieldPassword.setName("lblFieldPassword");
        pnlFieldtxtPassword.add(lblFieldPassword, java.awt.BorderLayout.NORTH);
        txtPassword.setColumns(18);
        txtPassword.setName("txtPassword");
        pnlFieldtxtPassword.add(txtPassword, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtPassword);
        pnlActions.setName("pnlActions");
        pnlActions.setLayout(new java.awt.FlowLayout(0,7,7));
        btnLogin.setText("Sign in");
        btnLogin.setName("btnLogin");
        pnlActions.add(btnLogin);
        btnExit.setText("Exit");
        btnExit.setName("btnExit");
        pnlActions.add(btnExit);
        pnlForm.add(pnlActions);
        lblStatus.setText("Your workspace for happier pets.");
        lblStatus.setName("lblStatus");
        pnlForm.add(lblStatus);
        pnlContent.add(pnlForm, java.awt.BorderLayout.CENTER);
        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);
        pack();
    }//GEN-END:initComponents
    // </editor-fold>
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JPanel pnlBrand;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblTagline;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JLabel lblEyebrow;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPanel pnlFieldtxtUsername;
    private javax.swing.JLabel lblFieldUsername;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JPanel pnlFieldtxtPassword;
    private javax.swing.JLabel lblFieldPassword;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnExit;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables
}
