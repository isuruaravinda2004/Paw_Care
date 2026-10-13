package com.pawcare;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import static org.junit.jupiter.api.Assertions.*;
import com.pawcare.view.*;
import com.pawcare.model.*;
import com.pawcare.util.*;
import com.pawcare.controller.LoginController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;
import java.util.function.*;
import javax.imageio.ImageIO;

@EnabledIfSystemProperty(named="pawcare.uiTests",matches="true")
class GuiSmokeTest extends DatabaseTest {
    private final List<String> warnings=new ArrayList<>();
    private final AWTEventListener dialogHandler=event -> {
        if(event instanceof WindowEvent && event.getID()==WindowEvent.WINDOW_OPENED && ((WindowEvent)event).getWindow() instanceof JDialog) {
            JDialog dialog=(JDialog)((WindowEvent)event).getWindow();
            SwingUtilities.invokeLater(() -> {
                JOptionPane pane=findPane(dialog);
                if(pane!=null) { if(pane.getMessageType()==JOptionPane.WARNING_MESSAGE) warnings.add(String.valueOf(pane.getMessage())); pane.setValue(JOptionPane.OK_OPTION); dialog.dispose(); }
            });
        }
    };
    @BeforeEach void setupUi() throws Exception {
        Main.configureTheme();
        Toolkit.getDefaultToolkit().addAWTEventListener(dialogHandler,AWTEvent.WINDOW_EVENT_MASK);
    }
    @AfterEach void closeUi() throws Exception {
        SwingUtilities.invokeAndWait(() -> { for(Window w:Window.getWindows()) w.dispose(); });
        Toolkit.getDefaultToolkit().removeAWTEventListener(dialogHandler); Session.clear();
    }
    @Test void opensNineScreensAndCapturesActualWindows() throws Exception {
        Session.setUser(new LoginController().login("admin","PawCare@123".toCharArray()));
        Files.createDirectories(Path.of("target/screenshots"));
        @SuppressWarnings("unchecked") Supplier<JFrame>[] makers=new Supplier[]{LoginFrame::new,DashboardFrame::new,CustomerFrame::new,PetFrame::new,ServiceFrame::new,AppointmentFrame::new,PaymentFrame::new,ReportFrame::new,BreedFrame::new};
        String[] names={"01-login","02-dashboard","03-customers","04-pets","05-services","06-appointments","07-payments","08-reports","09-breed-lookup"};
        for(int i=0;i<makers.length;i++) {
            Supplier<JFrame> make=makers[i]; JFrame[] box=new JFrame[1];
            SwingUtilities.invokeAndWait(() -> { box[0]=make.get(); box[0].setVisible(true); }); JFrame frame=box[0];
            await(() -> !text(frame,"lblStatus").startsWith("Loading"));
            assertTrue(warnings.isEmpty(),warnings.toString());
            final int index=i; SwingUtilities.invokeAndWait(() -> {
                try { BufferedImage image=new Robot().createScreenCapture(frame.getBounds()); ImageIO.write(image,"png",Path.of("target/screenshots",names[index]+".png").toFile()); }
                catch(Exception e) { throw new RuntimeException(e); }
                frame.dispose();
            });
        }
    }
    @Test void clicksLoginThroughBookingPaymentAndDashboard() throws Exception {
        SwingUtilities.invokeAndWait(() -> new LoginFrame().setVisible(true));
        JFrame login=active(LoginFrame.class);
        SwingUtilities.invokeAndWait(() -> { ((JPasswordField)find(login,"txtPassword")).setText("PawCare@123"); click(login,"btnLogin"); });
        await(() -> active(DashboardFrame.class)!=null); JFrame dashboard=active(DashboardFrame.class);
        await(() -> text(dashboard,"lblTotalCustomers").equals("2"));
        SwingUtilities.invokeAndWait(() -> click(dashboard,"btnNavCustomers"));
        JFrame customer=active(CustomerFrame.class); await(() -> text(customer,"lblStatus").startsWith("2 records"));
        SwingUtilities.invokeAndWait(() -> { input(customer,"txtCustomerName","UI Test Owner"); input(customer,"txtPhone","0773456789"); input(customer,"txtEmail","ui@example.com"); input(customer,"txtAddress","Galle"); click(customer,"btnSave"); });
        await(() -> text(customer,"lblStatus").startsWith("3 records"));
        SwingUtilities.invokeAndWait(() -> click(customer,"btnNavPets")); JFrame pet=active(PetFrame.class);
        await(() -> text(pet,"lblStatus").startsWith("3 records"));
        SwingUtilities.invokeAndWait(() -> { choose(pet,"cmbCustomer",v -> ((Customer)v).getName().equals("UI Test Owner")); input(pet,"txtPetName","UI Buddy"); input(pet,"txtBreed","Labrador"); input(pet,"txtAge","2"); click(pet,"btnSave"); });
        await(() -> text(pet,"lblStatus").startsWith("4 records"));
        SwingUtilities.invokeAndWait(() -> click(pet,"btnNavAppointments")); JFrame appointment=active(AppointmentFrame.class);
        await(() -> text(appointment,"lblStatus").startsWith("3 appointments"));
        SwingUtilities.invokeAndWait(() -> { choose(appointment,"cmbCustomer",v -> ((Customer)v).getName().equals("UI Test Owner")); choose(appointment,"cmbPet",v -> ((Pet)v).getPetName().equals("UI Buddy")); choose(appointment,"cmbService",v -> ((GroomingService)v).getServiceName().equals("Basic Bath")); input(appointment,"txtDate",AppClock.today().plusDays(4).toString()); input(appointment,"txtTime","15:00"); click(appointment,"btnSave"); });
        await(() -> text(appointment,"lblStatus").startsWith("4 appointments"));
        SwingUtilities.invokeAndWait(() -> click(appointment,"btnNavPayments")); JFrame payment=active(PaymentFrame.class);
        await(() -> text(payment,"lblStatus").startsWith("1 payments"));
        SwingUtilities.invokeAndWait(() -> { choose(payment,"cmbAppointment",v -> ((Appointment)v).getCustomerName().equals("UI Test Owner")); assertEquals("1500.00",((JTextField)find(payment,"txtAmount")).getText()); click(payment,"btnSave"); });
        await(() -> text(payment,"lblStatus").startsWith("2 payments"));
        SwingUtilities.invokeAndWait(() -> click(payment,"btnNavDashboard")); JFrame last=active(DashboardFrame.class);
        await(() -> text(last,"lblTodayRevenue").equals("Rs. 3000.00")); assertTrue(warnings.isEmpty(),warnings.toString());
    }
    private void await(BooleanSupplier condition) throws Exception {
        long end=System.currentTimeMillis()+15000;
        while(System.currentTimeMillis()<end) {
            boolean[] ok={false}; SwingUtilities.invokeAndWait(() -> ok[0]=condition.getAsBoolean());
            if(ok[0]) return; Thread.sleep(100);
        } fail("Timed out waiting for Swing/database workflow. Warnings: "+warnings);
    }
    private JFrame active(Class<?> type) { for(Window w:Window.getWindows()) if(type.isInstance(w) && w.isVisible()) return (JFrame)w; return null; }
    private Component find(Container parent,String name) {
        for(Component c:parent.getComponents()) { if(name.equals(c.getName())) return c; if(c instanceof Container) { Component found=find((Container)c,name); if(found!=null) return found; } } return null;
    }
    private JOptionPane findPane(Container parent) { for(Component c:parent.getComponents()) { if(c instanceof JOptionPane) return (JOptionPane)c; if(c instanceof Container) { JOptionPane p=findPane((Container)c); if(p!=null) return p; } } return null; }
    private String text(JFrame f,String name) { Component c=find(f,name); return c instanceof JLabel?((JLabel)c).getText():""; }
    private void input(JFrame f,String name,String value) { ((JTextField)find(f,name)).setText(value); }
    private void click(JFrame f,String name) { ((JButton)find(f,name)).doClick(); }
    private void choose(JFrame f,String name,Predicate<Object> test) { JComboBox<?> b=(JComboBox<?>)find(f,name); for(int i=0;i<b.getItemCount();i++) if(test.test(b.getItemAt(i))) { b.setSelectedIndex(i); return; } fail("Dropdown item not found: "+name); }
}
