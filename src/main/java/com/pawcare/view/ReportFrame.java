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
public class ReportFrame extends javax.swing.JFrame {

    private net.sf.jasperreports.engine.JasperPrint lastReport;
    public ReportFrame() {
        initComponents(); Ui.decorate(this,"Reports"); Ui.navigation(this,"Reports", btnNavDashboard, btnNavCustomers, btnNavPets, btnNavServices, btnNavAppointments, btnNavPayments, btnNavReports, btnNavBreed, btnNavLogout);
        txtFromDate.setText(AppClock.today().withDayOfMonth(1).toString()); txtToDate.setText(AppClock.today().plusMonths(1).toString());
        btnExport.setEnabled(false); btnGenerateAppointments.addActionListener(e -> generate("appointments",btnGenerateAppointments)); btnGeneratePayments.addActionListener(e -> generate("payments",btnGeneratePayments));
        btnExport.addActionListener(e -> export()); btnBack.addActionListener(e -> Ui.navigate(this,"Dashboard"));
    }
    private void generate(String name,JButton button) {
        try {
            LocalDate from=Validation.date(txtFromDate.getText()),to=Validation.date(txtToDate.getText());
            if(from.isAfter(to)) throw new ValidationException("From date must not be after To date.");
            lblStatus.setText("Preparing report...");
            Tasks.run(this,button,() -> new com.pawcare.report.ReportService().generate(name,from,to,null),report -> {
                lastReport=report; btnExport.setEnabled(true); lblStatus.setText("Report ready. Use the viewer toolbar to print or export."); net.sf.jasperreports.view.JasperViewer.viewReport(report,false);
            });
        } catch(Exception ex) { Ui.error(this,ex); }
    }
    private void export() {
        if(lastReport==null) return;
        JFileChooser chooser=new JFileChooser(); chooser.setSelectedFile(new java.io.File("PawCare-report.pdf"));
        if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) return;
        java.io.File chosen=chooser.getSelectedFile();
        final java.io.File file=chosen.getName().toLowerCase().endsWith(".pdf")?chosen:new java.io.File(chosen.getAbsolutePath()+".pdf");
        if(file.exists() && !Ui.confirm(this,"Replace the existing PDF?")) return;
        final net.sf.jasperreports.engine.JasperPrint report=lastReport;
        Tasks.run(this,btnExport,() -> { new com.pawcare.report.ReportService().exportPdf(report,file); return file; },saved -> Ui.message(this,"Saved: "+saved.getAbsolutePath()));
    }

    // Keep business logic outside the NetBeans guarded blocks.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {//GEN-BEGIN:initComponents
        pnlSidebar = new javax.swing.JPanel();
        pnlBrand = new javax.swing.JPanel();
        lblBrand = new javax.swing.JLabel();
        lblTagline = new javax.swing.JLabel();
        pnlMenu = new javax.swing.JPanel();
        btnNavDashboard = new javax.swing.JButton();
        btnNavCustomers = new javax.swing.JButton();
        btnNavPets = new javax.swing.JButton();
        btnNavServices = new javax.swing.JButton();
        btnNavAppointments = new javax.swing.JButton();
        btnNavPayments = new javax.swing.JButton();
        btnNavReports = new javax.swing.JButton();
        btnNavBreed = new javax.swing.JButton();
        btnNavLogout = new javax.swing.JButton();
        pnlContent = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblEyebrow = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlBodyWrap = new javax.swing.JPanel();
        pnlReportBody = new javax.swing.JPanel();
        pnlForm = new javax.swing.JPanel();
        pnlFieldtxtFromDate = new javax.swing.JPanel();
        lblFieldFromDate = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        pnlFieldtxtToDate = new javax.swing.JPanel();
        lblFieldToDate = new javax.swing.JLabel();
        txtToDate = new javax.swing.JTextField();
        lblDescription = new javax.swing.JLabel();
        pnlActions = new javax.swing.JPanel();
        btnGenerateAppointments = new javax.swing.JButton();
        btnGeneratePayments = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PawCare | Reports");
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
        pnlMenu.setName("pnlMenu");
        pnlMenu.setLayout(new java.awt.GridLayout(9,1,0,7));
        btnNavDashboard.setText("Dashboard");
        btnNavDashboard.setName("btnNavDashboard");
        pnlMenu.add(btnNavDashboard);
        btnNavCustomers.setText("Customers");
        btnNavCustomers.setName("btnNavCustomers");
        pnlMenu.add(btnNavCustomers);
        btnNavPets.setText("Pets");
        btnNavPets.setName("btnNavPets");
        pnlMenu.add(btnNavPets);
        btnNavServices.setText("Services");
        btnNavServices.setName("btnNavServices");
        pnlMenu.add(btnNavServices);
        btnNavAppointments.setText("Appointments");
        btnNavAppointments.setName("btnNavAppointments");
        pnlMenu.add(btnNavAppointments);
        btnNavPayments.setText("Payments");
        btnNavPayments.setName("btnNavPayments");
        pnlMenu.add(btnNavPayments);
        btnNavReports.setText("Reports");
        btnNavReports.setName("btnNavReports");
        pnlMenu.add(btnNavReports);
        btnNavBreed.setText("Breed lookup");
        btnNavBreed.setName("btnNavBreed");
        pnlMenu.add(btnNavBreed);
        btnNavLogout.setText("Logout");
        btnNavLogout.setName("btnNavLogout");
        pnlMenu.add(btnNavLogout);
        pnlSidebar.add(pnlMenu, java.awt.BorderLayout.CENTER);
        getContentPane().add(pnlSidebar, java.awt.BorderLayout.WEST);
        pnlContent.setName("pnlContent");
        pnlContent.setLayout(new java.awt.BorderLayout(10,10));
        pnlHeader.setName("pnlHeader");
        pnlHeader.setLayout(new java.awt.GridLayout(0,1,12,12));
        lblEyebrow.setText("PAWCARE / GROOMING CENTER");
        lblEyebrow.setName("lblEyebrow");
        pnlHeader.add(lblEyebrow);
        lblTitle.setText("Reports");
        lblTitle.setName("lblTitle");
        pnlHeader.add(lblTitle);
        lblSubtitle.setText("A clear view of visits and revenue.");
        lblSubtitle.setName("lblSubtitle");
        pnlHeader.add(lblSubtitle);
        pnlContent.add(pnlHeader, java.awt.BorderLayout.NORTH);
        pnlBodyWrap.setName("pnlBodyWrap");
        pnlBodyWrap.setLayout(new java.awt.BorderLayout(10,10));
        pnlReportBody.setName("pnlReportBody");
        pnlReportBody.setLayout(new java.awt.GridLayout(0,1,12,12));
        pnlForm.setName("pnlForm");
        pnlForm.setLayout(new java.awt.GridLayout(0,2,12,12));
        pnlFieldtxtFromDate.setName("pnlFieldtxtFromDate");
        pnlFieldtxtFromDate.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldFromDate.setText("From date (YYYY-MM-DD)");
        lblFieldFromDate.setName("lblFieldFromDate");
        pnlFieldtxtFromDate.add(lblFieldFromDate, java.awt.BorderLayout.NORTH);
        txtFromDate.setColumns(18);
        txtFromDate.setEditable(true);
        txtFromDate.setName("txtFromDate");
        pnlFieldtxtFromDate.add(txtFromDate, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtFromDate);
        pnlFieldtxtToDate.setName("pnlFieldtxtToDate");
        pnlFieldtxtToDate.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldToDate.setText("To date (YYYY-MM-DD)");
        lblFieldToDate.setName("lblFieldToDate");
        pnlFieldtxtToDate.add(lblFieldToDate, java.awt.BorderLayout.NORTH);
        txtToDate.setColumns(18);
        txtToDate.setEditable(true);
        txtToDate.setName("txtToDate");
        pnlFieldtxtToDate.add(txtToDate, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtToDate);
        pnlReportBody.add(pnlForm);
        lblDescription.setText("Preview appointments or payments for a date range. Print or save the result as a PDF.");
        lblDescription.setName("lblDescription");
        pnlReportBody.add(lblDescription);
        pnlActions.setName("pnlActions");
        pnlActions.setLayout(new java.awt.FlowLayout(0,7,7));
        btnGenerateAppointments.setText("Appointment report");
        btnGenerateAppointments.setName("btnGenerateAppointments");
        pnlActions.add(btnGenerateAppointments);
        btnGeneratePayments.setText("Payment report");
        btnGeneratePayments.setName("btnGeneratePayments");
        pnlActions.add(btnGeneratePayments);
        btnExport.setText("Save last report as PDF");
        btnExport.setName("btnExport");
        pnlActions.add(btnExport);
        btnBack.setText("Back to Dashboard");
        btnBack.setName("btnBack");
        pnlActions.add(btnBack);
        pnlReportBody.add(pnlActions);
        pnlBodyWrap.add(pnlReportBody, java.awt.BorderLayout.NORTH);
        pnlContent.add(pnlBodyWrap, java.awt.BorderLayout.CENTER);
        lblStatus.setText("Choose a date range to get started.");
        lblStatus.setName("lblStatus");
        pnlContent.add(lblStatus, java.awt.BorderLayout.SOUTH);
        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);
        pack();
    }//GEN-END:initComponents
    // </editor-fold>
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JPanel pnlBrand;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblTagline;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JButton btnNavDashboard;
    private javax.swing.JButton btnNavCustomers;
    private javax.swing.JButton btnNavPets;
    private javax.swing.JButton btnNavServices;
    private javax.swing.JButton btnNavAppointments;
    private javax.swing.JButton btnNavPayments;
    private javax.swing.JButton btnNavReports;
    private javax.swing.JButton btnNavBreed;
    private javax.swing.JButton btnNavLogout;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JLabel lblEyebrow;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JPanel pnlBodyWrap;
    private javax.swing.JPanel pnlReportBody;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPanel pnlFieldtxtFromDate;
    private javax.swing.JLabel lblFieldFromDate;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JPanel pnlFieldtxtToDate;
    private javax.swing.JLabel lblFieldToDate;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JButton btnGenerateAppointments;
    private javax.swing.JButton btnGeneratePayments;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables
}
