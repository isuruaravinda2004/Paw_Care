package com.pawcare.util;
import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import com.pawcare.view.*;
import com.pawcare.model.*;

public final class Ui {
    private Ui() { }
    public static final Color NAVY=new Color(20,43,63), BLUE=new Color(23,103,180), GREEN=new Color(9,126,107), RED=new Color(181,53,66), BG=new Color(242,246,249);
    public static void decorate(JFrame frame,String active) {
        frame.setMinimumSize(new Dimension(1080,720)); frame.setSize(1180,790); frame.setLocationRelativeTo(null);
        style(frame.getContentPane());
        frame.setIconImage(iconImage());
    }
    public static void style(Component item) {
        String name=item.getName()==null?"":item.getName();
        if(item instanceof JPanel) {
            JPanel p=(JPanel)item; p.setBackground(BG);
            if(name.equals("pnlContent")) p.setBorder(new EmptyBorder(22,26,20,26));
            if(name.equals("pnlSidebar")) { p.setBackground(NAVY); p.setBorder(new EmptyBorder(25,18,25,18)); p.setPreferredSize(new Dimension(202,500)); }
            if(name.equals("pnlMenu") || name.equals("pnlBrand")) p.setBackground(NAVY);
            if(name.equals("pnlForm")) p.setBorder(new CompoundBorder(new LineBorder(new Color(219,228,235)),new EmptyBorder(16,16,16,16)));
            if(name.startsWith("pnlCard")) { p.setBackground(Color.WHITE); p.setBorder(new CompoundBorder(new LineBorder(new Color(222,230,237)),new EmptyBorder(20,20,20,20))); }
        }
        if(item instanceof JLabel) {
            JLabel l=(JLabel)item; l.setForeground(NAVY);
            if(name.equals("lblTitle")) l.setFont(l.getFont().deriveFont(Font.BOLD,28));
            if(name.equals("lblEyebrow")) { l.setFont(l.getFont().deriveFont(Font.BOLD,11)); l.setForeground(GREEN); }
            if(name.equals("lblBrand")) { l.setForeground(Color.WHITE); l.setFont(l.getFont().deriveFont(Font.BOLD,25)); l.setIcon(new NavIcon(2)); }
            if(name.equals("lblTagline")) l.setForeground(new Color(176,199,213));
            if(name.equals("lblStatus") || name.equals("lblSubtitle")) l.setForeground(new Color(88,108,124));
            if(name.startsWith("lblTotal") || name.startsWith("lblToday") || name.startsWith("lblPending") || name.startsWith("lblCompleted")) { l.setForeground(BLUE); l.setFont(l.getFont().deriveFont(Font.BOLD,29)); }
        }
        if(item instanceof JButton) {
            JButton b=(JButton)item; b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); b.setMargin(new Insets(10,14,10,14));
            if(name.equals("btnSave") || name.equals("btnLogin") || name.equals("btnBook")) { b.setBackground(GREEN); b.setForeground(Color.WHITE); }
            if(name.equals("btnUpdate") || name.equals("btnSearch") || name.startsWith("btnGenerate")) { b.setBackground(BLUE); b.setForeground(Color.WHITE); }
            if(name.equals("btnDelete")) { b.setBackground(RED); b.setForeground(Color.WHITE); }
        }
        if(item instanceof JTextField) ((JTextField)item).setMargin(new Insets(8,10,8,10));
        if(item instanceof JComboBox) ((JComboBox<?>)item).setMaximumRowCount(12);
        if(item instanceof Container) for(Component child:((Container)item).getComponents()) style(child);
    }
    private static java.awt.image.BufferedImage iconImage() {
        java.awt.image.BufferedImage image=new java.awt.image.BufferedImage(32,32,java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=image.createGraphics(); g.setColor(GREEN); g.fillRoundRect(0,0,32,32,8,8); JLabel l=new JLabel(); l.setForeground(Color.WHITE); new NavIcon(2).paintIcon(l,g,5,4); g.dispose(); return image;
    }
    public static void navigation(JFrame from,String active,JButton... buttons) {
        String[] names={"Dashboard","Customers","Pets","Services","Appointments","Payments","Reports","Breed lookup","Logout"};
        for(int i=0;i<buttons.length;i++) {
            final String target=names[i]; JButton b=buttons[i];
            b.setIcon(new NavIcon(i)); b.setHorizontalAlignment(SwingConstants.LEFT); b.setIconTextGap(10);
            b.setForeground(Color.WHITE); b.setBackground(target.equals(active)?new Color(33,81,109):NAVY); b.setBorderPainted(false);
            b.addActionListener(e -> navigate(from,target));
        }
    }
    public static void navigate(JFrame from,String target) {
        if(target.equals("Logout")) {
            if(!confirm(from,"Log out of PawCare?")) return; Session.clear();
        } else if(Session.getUser()==null) { error(from,new IllegalStateException("Please log in first.")); return; }
        JFrame next;
        switch(target) {
            case "Dashboard": next=new DashboardFrame(); break;
            case "Customers": next=new CustomerFrame(); break;
            case "Pets": next=new PetFrame(); break;
            case "Services": next=new ServiceFrame(); break;
            case "Appointments": next=new AppointmentFrame(); break;
            case "Payments": next=new PaymentFrame(); break;
            case "Reports": next=new ReportFrame(); break;
            case "Breed lookup": next=new BreedFrame(); break;
            default: next=new LoginFrame();
        }
        next.setVisible(true); from.dispose();
    }
    public static DefaultTableModel table(JTable table,String... columns) {
        DefaultTableModel model=new DefaultTableModel(columns,0) { @Override public boolean isCellEditable(int row,int col) { return false; } };
        table.setModel(model); table.setRowHeight(34); table.setAutoCreateRowSorter(true); table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true); table.setShowVerticalLines(false); table.setSelectionBackground(new Color(213,232,245)); table.setSelectionForeground(NAVY);
        table.getTableHeader().setFont(table.getFont().deriveFont(Font.BOLD)); table.getTableHeader().setPreferredSize(new Dimension(100,36)); return model;
    }
    public static void select(JComboBox<?> box,int id) {
        for(int i=0;i<box.getItemCount();i++) {
            Object v=box.getItemAt(i); int current=v instanceof Customer?((Customer)v).getId():v instanceof Pet?((Pet)v).getId():v instanceof GroomingService?((GroomingService)v).getId():v instanceof Appointment?((Appointment)v).getId():-1;
            if(current==id) { box.setSelectedIndex(i); return; }
        }
        box.setSelectedIndex(-1);
    }
    public static int id(JComboBox<?> box) {
        Object v=box.getSelectedItem();
        return v instanceof Customer?((Customer)v).getId():v instanceof Pet?((Pet)v).getId():v instanceof GroomingService?((GroomingService)v).getId():v instanceof Appointment?((Appointment)v).getId():0;
    }
    public static boolean confirm(Component parent,String message) { return JOptionPane.showConfirmDialog(parent,message,"PawCare",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION; }
    public static void message(Component parent,String message) { JOptionPane.showMessageDialog(parent,message,"PawCare",JOptionPane.INFORMATION_MESSAGE); }
    public static void error(Component parent,Throwable ex) {
        if(parent instanceof Container) showFailureStatus((Container)parent);
        ex.printStackTrace(); String message=ex.getMessage();
        if(ex instanceof SQLException) {
            String state=((SQLException)ex).getSQLState();
            if(state!=null && state.startsWith("08")) message="Cannot connect to MySQL. Start MySQL and check config.properties (host, port, database, username and password).";
            else if(state!=null && state.startsWith("23")) message="This change conflicts with an existing record. Names and payments must be unique where required. Delete linked records before deleting a customer, pet, service or appointment.";
            else if(state!=null && state.startsWith("42")) message="Check database.sql was imported into pet_grooming_db and the database user has permission. Details: "+ex.getMessage();
            else if(state!=null && state.startsWith("28")) message="MySQL rejected the username/password. Check config.properties.";
        }
        JOptionPane.showMessageDialog(parent,message==null?"The operation failed. Check the console for details.":message,"Please check",JOptionPane.WARNING_MESSAGE);
    }
    private static void showFailureStatus(Container parent) {
        for(Component child:parent.getComponents()) {
            if(child instanceof JLabel && "lblStatus".equals(child.getName()))
                ((JLabel)child).setText("Operation failed. Check the message, then try again.");
            if(child instanceof Container) showFailureStatus((Container)child);
        }
    }
}
