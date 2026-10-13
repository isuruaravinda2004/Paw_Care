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
public class BreedFrame extends javax.swing.JFrame {

    private final BreedApiService api=new BreedApiService();
    private List<String> breeds=new ArrayList<>();
    public BreedFrame() {
        initComponents(); Ui.decorate(this,"Breed lookup"); Ui.navigation(this,"Breed lookup", btnNavDashboard, btnNavCustomers, btnNavPets, btnNavServices, btnNavAppointments, btnNavPayments, btnNavReports, btnNavBreed, btnNavLogout);
        btnImage.setEnabled(false);
        btnLoadBreeds.addActionListener(e -> Tasks.run(this,btnLoadBreeds,() -> api.breeds(),data -> { breeds=data; filterBreeds(); lblStatus.setText(data.size()+" breeds and sub-breeds loaded • Source: dog.ceo"); }));
        txtBreedFilter.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterBreeds(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterBreeds(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterBreeds(); }
        });
        btnImage.addActionListener(e -> loadImage()); btnBack.addActionListener(e -> Ui.navigate(this,"Dashboard"));
    }
    private void filterBreeds() {
        cmbBreed.removeAllItems(); String query=txtBreedFilter.getText().trim().toLowerCase(java.util.Locale.ROOT);
        for(String breed:breeds) if(breed.contains(query)) cmbBreed.addItem(breed);
        btnImage.setEnabled(cmbBreed.getItemCount()>0);
    }
    private void loadImage() {
        String breed=(String)cmbBreed.getSelectedItem(); if(breed==null) return;
        Tasks.run(this,btnImage,() -> api.image(breed),image -> {
            double scale=Math.min(640.0/image.getWidth(),340.0/image.getHeight());
            lblBreedImage.setIcon(new ImageIcon(image.getScaledInstance((int)(image.getWidth()*scale),(int)(image.getHeight()*scale),java.awt.Image.SCALE_SMOOTH)));
            lblBreedImage.setText(""); lblStatus.setText("Breed: "+breed+" • Photo supplied by Dog CEO. These are reference images, not registered pets.");
        });
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
        pnlBreedControls = new javax.swing.JPanel();
        pnlForm = new javax.swing.JPanel();
        pnlFieldtxtBreedFilter = new javax.swing.JPanel();
        lblFieldBreedFilter = new javax.swing.JLabel();
        txtBreedFilter = new javax.swing.JTextField();
        pnlFieldcmbBreed = new javax.swing.JPanel();
        lblFieldBreed = new javax.swing.JLabel();
        cmbBreed = new javax.swing.JComboBox();
        pnlActions = new javax.swing.JPanel();
        btnLoadBreeds = new javax.swing.JButton();
        btnImage = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        lblBreedImage = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PawCare | Breed lookup");
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
        lblTitle.setText("Breed lookup");
        lblTitle.setName("lblTitle");
        pnlHeader.add(lblTitle);
        lblSubtitle.setText("Meet the dogs behind the names.");
        lblSubtitle.setName("lblSubtitle");
        pnlHeader.add(lblSubtitle);
        pnlTop.add(pnlHeader, java.awt.BorderLayout.NORTH);
        pnlBreedControls.setName("pnlBreedControls");
        pnlBreedControls.setLayout(new java.awt.GridLayout(0,1,12,12));
        pnlForm.setName("pnlForm");
        pnlForm.setLayout(new java.awt.GridLayout(0,2,12,12));
        pnlFieldtxtBreedFilter.setName("pnlFieldtxtBreedFilter");
        pnlFieldtxtBreedFilter.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldBreedFilter.setText("Filter breeds");
        lblFieldBreedFilter.setName("lblFieldBreedFilter");
        pnlFieldtxtBreedFilter.add(lblFieldBreedFilter, java.awt.BorderLayout.NORTH);
        txtBreedFilter.setColumns(18);
        txtBreedFilter.setEditable(true);
        txtBreedFilter.setName("txtBreedFilter");
        pnlFieldtxtBreedFilter.add(txtBreedFilter, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldtxtBreedFilter);
        pnlFieldcmbBreed.setName("pnlFieldcmbBreed");
        pnlFieldcmbBreed.setLayout(new java.awt.BorderLayout(10,10));
        lblFieldBreed.setText("Breed / sub-breed");
        lblFieldBreed.setName("lblFieldBreed");
        pnlFieldcmbBreed.add(lblFieldBreed, java.awt.BorderLayout.NORTH);
        cmbBreed.setName("cmbBreed");
        pnlFieldcmbBreed.add(cmbBreed, java.awt.BorderLayout.CENTER);
        pnlForm.add(pnlFieldcmbBreed);
        pnlBreedControls.add(pnlForm);
        pnlActions.setName("pnlActions");
        pnlActions.setLayout(new java.awt.FlowLayout(0,7,7));
        btnLoadBreeds.setText("Load breeds");
        btnLoadBreeds.setName("btnLoadBreeds");
        pnlActions.add(btnLoadBreeds);
        btnImage.setText("Show breed image");
        btnImage.setName("btnImage");
        pnlActions.add(btnImage);
        btnBack.setText("Back to Dashboard");
        btnBack.setName("btnBack");
        pnlActions.add(btnBack);
        pnlBreedControls.add(pnlActions);
        pnlTop.add(pnlBreedControls, java.awt.BorderLayout.CENTER);
        pnlContent.add(pnlTop, java.awt.BorderLayout.NORTH);
        lblBreedImage.setText("Load the breed list, then choose a dog breed.");
        lblBreedImage.setHorizontalAlignment(0);
        lblBreedImage.setName("lblBreedImage");
        pnlContent.add(lblBreedImage, java.awt.BorderLayout.CENTER);
        lblStatus.setText("Images from Dog CEO \u2022 Internet required only for this optional feature.");
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
    private javax.swing.JPanel pnlBreedControls;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JPanel pnlFieldtxtBreedFilter;
    private javax.swing.JLabel lblFieldBreedFilter;
    private javax.swing.JTextField txtBreedFilter;
    private javax.swing.JPanel pnlFieldcmbBreed;
    private javax.swing.JLabel lblFieldBreed;
    private javax.swing.JComboBox cmbBreed;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JButton btnLoadBreeds;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnBack;
    private javax.swing.JLabel lblBreedImage;
    private javax.swing.JLabel lblStatus;
    // End of variables declaration//GEN-END:variables
}
