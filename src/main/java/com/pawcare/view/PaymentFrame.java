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
public class PaymentFrame extends javax.swing.JFrame {

    private final PaymentController controller=new PaymentController();
    private List<Payment> rows=new ArrayList<>();
    private DefaultTableModel tableModel;
    private int selectedId=0,loadVersion=0;
    public PaymentFrame() {
        initComponents(); Ui.decorate(this,"Payments"); Ui.navigation(this,"Payments", btnNavDashboard, btnNavCustomers, btnNavPets, btnNavServices, btnNavAppointments, btnNavPayments, btnNavReports, btnNavBreed, btnNavLogout);
        cmbMethod.setModel(new DefaultComboBoxModel(new String[]{"Cash","Card","Bank Transfer"}));
        tableModel=Ui.table(tblPayments,"ID","Appointment","Customer","Service","Amount (LKR)","Method","Date");
        cmbAppointment.addActionListener(e -> appointmentChanged());
        btnSave.addActionListener(e -> save(false)); btnUpdate.addActionListener(e -> save(true)); btnDelete.addActionListener(e -> remove());
        btnSearch.addActionListener(e -> refresh()); txtSearch.addActionListener(e -> refresh());
        btnClear.addActionListener(e -> { txtSearch.setText(""); clear(); refresh(); }); btnBack.addActionListener(e -> Ui.navigate(this,"Dashboard")); btnReceipt.addActionListener(e -> receipt());
        tblPayments.getSelectionModel().addListSelectionListener(e -> { if(!e.getValueIsAdjusting()) selectRow(); });
        clear(); refresh();
    }
    private void refresh() {
        final int version=++loadVersion; String search=txtSearch.getText(); lblStatus.setText("Loading payments...");
        Tasks.run(this,btnSearch,() -> new Data(controller.list(search),new LookupController().appointments()),data -> {
            if(version!=loadVersion) return;
            rows=data.payments; cmbAppointment.removeAllItems();
            for(Appointment a:data.appointments) if(!"Cancelled".equals(a.getStatus())) cmbAppointment.addItem(a);
            tableModel.setRowCount(0);
            for(Payment p:rows) tableModel.addRow(new Object[]{p.getId(),p.getAppointmentId(),p.getCustomerName(),p.getServiceName(),p.getAmount(),p.getMethod(),p.getDate()});
            clear(); lblStatus.setText(rows.size()+" payments • One full payment per appointment • Select a payment for its receipt.");
        });
    }
    private void appointmentChanged() {
        Appointment a=(Appointment)cmbAppointment.getSelectedItem();
        txtCustomer.setText(a==null?"":a.getCustomerName()); txtService.setText(a==null?"":a.getServiceName()); txtAmount.setText(a==null?"":a.getBookedPrice().toPlainString());
    }
    private void save(boolean update) {
        try {
            if(update && selectedId==0) throw new InvalidPaymentException("Select a payment to update.");
            Payment p=new Payment(); p.setId(update?selectedId:0); p.setAppointmentId(Ui.id(cmbAppointment)); p.setAmount(Validation.money(txtAmount.getText(),"Amount")); p.setMethod((String)cmbMethod.getSelectedItem()); p.setDate(Validation.date(txtDate.getText()));
            Tasks.run(this,update?btnUpdate:btnSave,() -> { controller.save(p); return p; },saved -> { txtSearch.setText(""); refresh(); Ui.message(this,"Payment saved. Select the saved row to print its receipt."); });
        } catch(Exception e) { Ui.error(this,e); }
    }
    private void remove() {
        if(selectedId==0) { Ui.message(this,"Select a payment first."); return; }
        int id=selectedId; if(!Ui.confirm(this,"Delete this payment? Dashboard revenue will decrease.")) return;
        Tasks.run(this,btnDelete,() -> { controller.delete(id); return true; },ok -> refresh());
    }
    private void receipt() {
        if(selectedId==0) { Ui.message(this,"Select a saved payment to print."); return; }
        int id=selectedId;
        Tasks.run(this,btnReceipt,() -> new com.pawcare.report.ReportService().generate("receipt",AppClock.today(),AppClock.today(),id),report -> net.sf.jasperreports.view.JasperViewer.viewReport(report,false));
    }
    private void selectRow() {
        int row=tblPayments.getSelectedRow(); if(row<0) return;
        Payment p=rows.get(tblPayments.convertRowIndexToModel(row)); selectedId=p.getId(); txtPaymentId.setText(""+p.getId()); Ui.select(cmbAppointment,p.getAppointmentId()); cmbAppointment.setEnabled(false);
        txtAmount.setText(p.getAmount().toPlainString()); cmbMethod.setSelectedItem(p.getMethod()); txtDate.setText(p.getDate().toString());
    }
    private void clear() { selectedId=0; tblPayments.clearSelection(); txtPaymentId.setText("New"); cmbAppointment.setEnabled(true); cmbAppointment.setSelectedIndex(-1); txtCustomer.setText(""); txtService.setText(""); txtAmount.setText(""); cmbMethod.setSelectedIndex(0); txtDate.setText(AppClock.today().toString()); }
    private static class Data {
        final List<Payment> payments; final List<Appointment> appointments;
        Data(List<Payment> p,List<Appointment> a) { payments=p;appointments=a; }
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
        pnlTop = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblEyebrow = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlForm = new javax.swing.JPanel();
        pnlFieldtxtPaymentId = new javax.swing.JPanel();
        lblFieldPaymentId = new javax.swing.JLabel();
        txtPaymentId = new javax.swing.JTextField();
        pnlFieldcmbAppointment = new javax.swing.JPanel();
        lblFieldAppointment = new javax.swing.JLabel();
        cmbAppointment = new javax.swing.JComboBox();
        pnlFieldtxtCustomer = new javax.swing.JPanel();
        lblFieldCustomer = new javax.swing.JLabel();
        txtCustomer = new javax.swing.JTextField();
        pnlFieldtxtService = new javax.swing.JPanel();
        lblFieldService = new javax.swing.JLabel();
        txtService = new javax.swing.JTextField();
        pnlFieldtxtAmount = new javax.swing.JPanel();
        lblFieldAmount = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        pnlFieldcmbMethod = new javax.swing.JPanel();
        lblFieldMethod = new javax.swing.JLabel();
        cmbMethod = new javax.swing.JComboBox();
        pnlFieldtxtDate = new javax.swing.JPanel();
        lblFieldDate = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        pnlTools = new javax.swing.JPanel();
        pnlActions = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnReceipt = new javax.swing.JButton();
        pnlSearch = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        scrollTable = new javax.swing.JScrollPane();
        tblPayments = new javax.swing.JTable();
        lblStatus = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PawCare | Payments");
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
        pnlTop.setName("pnlTop");
        pnlTop.setLayout(new java.awt.BorderLayout(10,10));
        pnlHeader.setName("pnlHeader");
        pnlHeader.setLayout(new java.awt.GridLayout(0,1,12,12));
        lblEyebrow.setText("PAWCARE / GROOMING CENTER");
        lblEyebrow.setName("lblEyebrow");
        pnlHeader.add(lblEyebrow);
        lblTitle.setText("Payments");
        lblTitle.setName("lblTitle");
        pnlHeader.add(lblTitle);
        lblSubtitle.setText("Record a payment, keep your revenue up to date.");
        lblSubtitle.setName("lblSubtitle");
        pnlHeader.add(lblSubtitle);
        pnlTop.add(pnlHeader, java.awt.BorderLayout.NORTH);
        pnlForm.setName("pnlForm");
        pnlForm.setLayout(new java.awt.GridLayout(0,3,12,12));
        pnlFieldtxtPaymentId.setName("pnlFieldtxtPaymentId");
        pnlFieldtxtPaymentId.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldPaymentId.setText("Payment ID");
        lblFieldPaymentId.setName("lblFieldPaymentId");
        pnlFieldtxtPaymentId.add(lblFieldPaymentId, java.awt.BorderLayout.NORTH);
        txtPaymentId.setColumns(18);
        txtPaymentId.setEditable(false);
        txtPaymentId.setName("txtPaymentId");
        pnlFieldtxtPaymentId.add(txtPaymentId, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtPaymentId);
        pnlFieldcmbAppointment.setName("pnlFieldcmbAppointment");
        pnlFieldcmbAppointment.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldAppointment.setText("Appointment");
        lblFieldAppointment.setName("lblFieldAppointment");
        pnlFieldcmbAppointment.add(lblFieldAppointment, java.awt.BorderLayout.NORTH);
        cmbAppointment.setName("cmbAppointment");
        pnlFieldcmbAppointment.add(cmbAppointment, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldcmbAppointment);
        pnlFieldtxtCustomer.setName("pnlFieldtxtCustomer");
        pnlFieldtxtCustomer.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldCustomer.setText("Customer");
        lblFieldCustomer.setName("lblFieldCustomer");
        pnlFieldtxtCustomer.add(lblFieldCustomer, java.awt.BorderLayout.NORTH);
        txtCustomer.setColumns(18);
        txtCustomer.setEditable(false);
        txtCustomer.setName("txtCustomer");
        pnlFieldtxtCustomer.add(txtCustomer, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtCustomer);
        pnlFieldtxtService.setName("pnlFieldtxtService");
        pnlFieldtxtService.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldService.setText("Service");
        lblFieldService.setName("lblFieldService");
        pnlFieldtxtService.add(lblFieldService, java.awt.BorderLayout.NORTH);
        txtService.setColumns(18);
        txtService.setEditable(false);
        txtService.setName("txtService");
        pnlFieldtxtService.add(txtService, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtService);
        pnlFieldtxtAmount.setName("pnlFieldtxtAmount");
        pnlFieldtxtAmount.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldAmount.setText("Amount (LKR, full payment)");
        lblFieldAmount.setName("lblFieldAmount");
        pnlFieldtxtAmount.add(lblFieldAmount, java.awt.BorderLayout.NORTH);
        txtAmount.setColumns(18);
        txtAmount.setEditable(true);
        txtAmount.setName("txtAmount");
        pnlFieldtxtAmount.add(txtAmount, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtAmount);
        pnlFieldcmbMethod.setName("pnlFieldcmbMethod");
        pnlFieldcmbMethod.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldMethod.setText("Payment method");
        lblFieldMethod.setName("lblFieldMethod");
        pnlFieldcmbMethod.add(lblFieldMethod, java.awt.BorderLayout.NORTH);
        cmbMethod.setName("cmbMethod");
        pnlFieldcmbMethod.add(cmbMethod, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldcmbMethod);
        pnlFieldtxtDate.setName("pnlFieldtxtDate");
        pnlFieldtxtDate.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldDate.setText("Payment date (YYYY-MM-DD)");
        lblFieldDate.setName("lblFieldDate");
        pnlFieldtxtDate.add(lblFieldDate, java.awt.BorderLayout.NORTH);
        txtDate.setColumns(18);
        txtDate.setEditable(true);
        txtDate.setName("txtDate");
        pnlFieldtxtDate.add(txtDate, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtDate);
        pnlTop.add(pnlForm, java.awt.BorderLayout.CENTER);
        pnlTools.setName("pnlTools");
        pnlTools.setLayout(new java.awt.BorderLayout(10,10));
        pnlActions.setName("pnlActions");
        pnlActions.setLayout(new java.awt.FlowLayout(0,7,7));
        btnSave.setText("Make payment");
        btnSave.setName("btnSave");
        pnlActions.add(btnSave);
        btnUpdate.setText("Update");
        btnUpdate.setName("btnUpdate");
        pnlActions.add(btnUpdate);
        btnDelete.setText("Delete");
        btnDelete.setName("btnDelete");
        pnlActions.add(btnDelete);
        btnReceipt.setText("Print receipt");
        btnReceipt.setName("btnReceipt");
        pnlActions.add(btnReceipt);
        pnlTools.add(pnlActions, java.awt.BorderLayout.NORTH);
        pnlSearch.setName("pnlSearch");
        pnlSearch.setLayout(new java.awt.FlowLayout(0,7,7));
        txtSearch.setColumns(18);
        txtSearch.setEditable(true);
        txtSearch.setName("txtSearch");
        pnlSearch.add(txtSearch);
        btnSearch.setText("Search");
        btnSearch.setName("btnSearch");
        pnlSearch.add(btnSearch);
        btnClear.setText("Clear form");
        btnClear.setName("btnClear");
        pnlSearch.add(btnClear);
        btnBack.setText("Back to Dashboard");
        btnBack.setName("btnBack");
        pnlSearch.add(btnBack);
        pnlTools.add(pnlSearch, java.awt.BorderLayout.SOUTH);
        pnlTop.add(pnlTools, java.awt.BorderLayout.SOUTH);
        pnlContent.add(pnlTop, java.awt.BorderLayout.NORTH);
        scrollTable.setName("scrollTable");
        tblPayments.setName("tblPayments");
        scrollTable.setViewportView(tblPayments);
        pnlContent.add(scrollTable, java.awt.BorderLayout.CENTER);
        lblStatus.setText("Loading records...");
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
    private javax.swing.JPanel pnlTop;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JLabel lblEyebrow;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPanel pnlFieldtxtPaymentId;
    private javax.swing.JLabel lblFieldPaymentId;
    private javax.swing.JTextField txtPaymentId;
    private javax.swing.JPanel pnlFieldcmbAppointment;
    private javax.swing.JLabel lblFieldAppointment;
    private javax.swing.JComboBox cmbAppointment;
    private javax.swing.JPanel pnlFieldtxtCustomer;
    private javax.swing.JLabel lblFieldCustomer;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JPanel pnlFieldtxtService;
    private javax.swing.JLabel lblFieldService;
    private javax.swing.JTextField txtService;
    private javax.swing.JPanel pnlFieldtxtAmount;
    private javax.swing.JLabel lblFieldAmount;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JPanel pnlFieldcmbMethod;
    private javax.swing.JLabel lblFieldMethod;
    private javax.swing.JComboBox cmbMethod;
    private javax.swing.JPanel pnlFieldtxtDate;
    private javax.swing.JLabel lblFieldDate;
    private javax.swing.JTextField txtDate;
    private javax.swing.JPanel pnlTools;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnReceipt;
    private javax.swing.JPanel pnlSearch;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnBack;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTable tblPayments;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables
}
