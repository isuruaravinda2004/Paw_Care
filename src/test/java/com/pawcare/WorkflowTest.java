package com.pawcare;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.pawcare.controller.*;
import com.pawcare.dao.*;
import com.pawcare.model.*;
import com.pawcare.exception.*;
import com.pawcare.util.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.concurrent.*;

class WorkflowTest extends DatabaseTest {
    private Appointment booking(LocalTime time) {
        Appointment a=new Appointment(); a.setCustomerId(1); a.setPetId(1); a.setServiceId(1); a.setDate(AppClock.today().plusDays(5)); a.setTime(time); a.setStatus("Confirmed"); return a;
    }
    private Payment payment(int appointmentId) {
        Payment p=new Payment(); p.setAppointmentId(appointmentId); p.setAmount(new BigDecimal("1500.00")); p.setDate(AppClock.today()); p.setMethod("Cash"); return p;
    }
    @Test void loginAndPasswordChange() throws Exception {
        LoginController ctrl=new LoginController(); User u=ctrl.login("admin","PawCare@123".toCharArray()); assertEquals("Admin",u.getRole());
        assertThrows(ValidationException.class,() -> ctrl.login("admin' OR '1'='1","bad".toCharArray()));
        assertThrows(ValidationException.class,() -> ctrl.login("admin","bad".toCharArray()));
        ctrl.changePassword(u,"PawCare@123".toCharArray(),"NewDemo!456".toCharArray());
        assertNotNull(ctrl.login("admin","NewDemo!456".toCharArray()));
        assertThrows(ValidationException.class,() -> ctrl.login("admin","PawCare@123".toCharArray()));
    }
    @Test void customerPetServiceCrudAndForeignKeys() throws Exception {
        CustomerController cc=new CustomerController(); Customer c=new Customer(); c.setName("Test Owner"); c.setPhone("0779876543"); c.setEmail("test@example.com"); c.setAddress("Galle"); cc.save(c); assertTrue(c.getId()>0);
        c.setName("Edited Owner"); cc.save(c); assertEquals(1,cc.list("Edited Owner").size());
        PetController pc=new PetController(); Pet p=new Pet(); p.setCustomerId(c.getId()); p.setPetName("Test Pet"); p.setBreed("Mixed"); p.setAnimalType("Dog"); p.setGender("Male"); p.setAge(2); pc.save(p);
        assertThrows(java.sql.SQLException.class,() -> cc.delete(c.getId()));
        p.setPetName("Edited Pet"); pc.save(p); assertEquals("Edited Pet",pc.list("Edited Pet").get(0).getPetName());
        ServiceController sc=new ServiceController(); GroomingService service=new GroomingService(); service.setServiceName("Test Service"); service.setDescription("Test"); service.setPrice(new BigDecimal("999.00")); service.setDurationMinutes(30); sc.save(service);
        service.setPrice(new BigDecimal("1099.00")); sc.save(service); assertEquals(new BigDecimal("1099.00"),sc.list("Test Service").get(0).getPrice());
        sc.delete(service.getId()); pc.delete(p.getId()); cc.delete(c.getId()); assertTrue(cc.list("Edited Owner").isEmpty());
    }
    @Test void appointmentPaymentWorkflowAndDashboard() throws Exception {
        AppointmentController ac=new AppointmentController(); Appointment a=booking(LocalTime.of(14,0)); ac.save(a);
        assertEquals(new BigDecimal("1500.00"),a.getBookedPrice());
        a.setStatus("Pending"); ac.save(a); assertEquals("Pending",new AppointmentDAO().findById(a.getId()).getStatus());
        PaymentController pc=new PaymentController(); Payment p=payment(a.getId()); pc.save(p);
        assertEquals("Rs. 3000.00",new DashboardDAO().load()[5]);
        assertThrows(InvalidPaymentException.class,() -> pc.save(payment(a.getId())));
        assertThrows(InvalidAppointmentException.class,() -> ac.cancel(a.getId()));
        assertThrows(InvalidAppointmentException.class,() -> ac.delete(a.getId()));
        p.setMethod("Card"); pc.save(p); assertEquals("Card",pc.list("Card").get(0).getMethod());
        pc.delete(p.getId()); assertEquals("Rs. 1500.00",new DashboardDAO().load()[5]);
        ac.cancel(a.getId()); assertEquals("Cancelled",new AppointmentDAO().findById(a.getId()).getStatus());
        assertThrows(InvalidPaymentException.class,() -> pc.save(payment(a.getId())));
        ac.delete(a.getId()); assertNull(new AppointmentDAO().findById(a.getId()));
    }
    @Test void appointmentConflictsAndAdjacentSlots() throws Exception {
        AppointmentController ac=new AppointmentController(); Appointment first=booking(LocalTime.of(14,0)); ac.save(first);
        assertThrows(InvalidAppointmentException.class,() -> ac.save(booking(LocalTime.of(14,30))));
        assertThrows(InvalidAppointmentException.class,() -> ac.save(booking(LocalTime.of(13,45))));
        Appointment adjacent=booking(LocalTime.of(14,45)); ac.save(adjacent); assertTrue(adjacent.getId()>0);
        ac.cancel(first.getId()); Appointment replacement=booking(LocalTime.of(14,0)); ac.save(replacement);
    }
    @Test void rejectsInvalidOwnerPastTimeAndWrongPayment() throws Exception {
        AppointmentController ac=new AppointmentController(); Appointment a=booking(LocalTime.of(14,0)); a.setCustomerId(2);
        assertThrows(InvalidAppointmentException.class,() -> ac.save(a)); a.setCustomerId(1); a.setDate(AppClock.today().minusDays(1));
        assertThrows(InvalidAppointmentException.class,() -> ac.save(a)); a.setDate(AppClock.today().plusDays(5)); a.setTime(LocalTime.of(23,40));
        assertThrows(InvalidAppointmentException.class,() -> ac.save(a)); a.setTime(LocalTime.of(14,0)); ac.save(a);
        Payment p=payment(a.getId()); p.setAmount(new BigDecimal("1"));
        assertThrows(InvalidPaymentException.class,() -> new PaymentController().save(p));
        p.setAmount(new BigDecimal("1500")); p.setDate(AppClock.today().plusDays(1));
        assertThrows(InvalidPaymentException.class,() -> new PaymentController().save(p));
    }
    @Test void preservesPriceWhenServiceMenuChanges() throws Exception {
        Appointment a=booking(LocalTime.of(14,0)); AppointmentController ac=new AppointmentController(); ac.save(a);
        GroomingService service=new ServiceDAO().findById(1); service.setPrice(new BigDecimal("1700.00")); new ServiceController().save(service);
        a.setStatus("Pending"); ac.save(a); assertEquals(new BigDecimal("1500.00"),new AppointmentDAO().findById(a.getId()).getBookedPrice());
        Appointment b=booking(LocalTime.of(16,0)); ac.save(b); assertEquals(new BigDecimal("1700.00"),b.getBookedPrice());
    }
    @Test void concurrentBookingsCannotTakeSameSlot() throws Exception {
        ExecutorService pool=Executors.newFixedThreadPool(2); CountDownLatch go=new CountDownLatch(1);
        Callable<Boolean> task=() -> { go.await(); try { new AppointmentController().save(booking(LocalTime.of(14,0))); return true; } catch(InvalidAppointmentException ex) { return false; } };
        try { Future<Boolean> one=pool.submit(task),two=pool.submit(task); go.countDown(); int successes=(one.get(15,TimeUnit.SECONDS)?1:0)+(two.get(15,TimeUnit.SECONDS)?1:0); assertEquals(1,successes); }
        finally { pool.shutdownNow(); }
    }
}
