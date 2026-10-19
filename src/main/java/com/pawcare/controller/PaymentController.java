package com.pawcare.controller;
import com.pawcare.dao.PaymentDAO;
import com.pawcare.model.Payment;
import com.pawcare.exception.*;
import com.pawcare.service.Validation;
import com.pawcare.util.AppClock;
import java.sql.SQLException;
import java.util.List;
public class PaymentController {
    private final PaymentDAO dao=new PaymentDAO();
    public List<Payment> list(String search) throws SQLException { return dao.findAll(search); }
    public void save(Payment p) throws SQLException, ValidationException {
        if(p.getAppointmentId()<=0) throw new InvalidPaymentException("Select an appointment before making a payment.");
        if(p.getAmount()==null || p.getAmount().signum()<=0) throw new InvalidPaymentException("Payment amount must be greater than zero.");
        p.setAmount(Validation.money(p.getAmount().toPlainString(),"Amount"));
        Validation.choice(p.getMethod(),"payment method","Cash","Card","Bank Transfer");
        if(p.getDate()==null || p.getDate().isAfter(AppClock.today())) throw new InvalidPaymentException("Payment date must be today or an earlier date.");
        dao.save(p);
    }
    public void delete(int id) throws SQLException, ValidationException {
        if(id<=0) throw new InvalidPaymentException("Select a payment row first."); dao.delete(id);
    }
}
