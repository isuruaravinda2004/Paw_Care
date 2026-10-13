package com.pawcare;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.pawcare.service.*;
import com.pawcare.exception.*;
import com.pawcare.util.PasswordUtil;
import com.pawcare.controller.CustomerController;
import com.pawcare.model.Customer;
import java.time.*;
class ValidationTest {
    @Test void numbersDatesTimesAndChoices() throws Exception {
        assertEquals(LocalTime.of(14,30),Validation.time("14:30"));
        assertThrows(ValidationException.class,() -> Validation.time("25:00"));
        assertThrows(ValidationException.class,() -> Validation.date("2026-02-30"));
        assertThrows(ValidationException.class,() -> Validation.money("0","Amount"));
        assertThrows(ValidationException.class,() -> Validation.money("12.123","Amount"));
        assertThrows(ValidationException.class,() -> Validation.integer("two","Age",1,100));
        assertThrows(ValidationException.class,() -> Validation.choice("Bitcoin","payment method","Cash","Card"));
    }
    @Test void phoneAndEmailAreChecked() throws Exception {
        Customer c=new Customer(); c.setName("Owner"); c.setPhone("123"); c.setEmail("a@example.com"); c.setAddress("Galle");
        CustomerController controller=new CustomerController(); assertThrows(ValidationException.class,() -> controller.validate(c));
        c.setPhone("0771234567"); c.setEmail("broken"); assertThrows(ValidationException.class,() -> controller.validate(c));
        c.setEmail("owner@example.com"); assertDoesNotThrow(() -> controller.validate(c));
    }
    @Test void saltedPasswordsAndMalformedHash() {
        String one=PasswordUtil.hash("Test@123".toCharArray()),two=PasswordUtil.hash("Test@123".toCharArray());
        assertNotEquals(one,two); assertTrue(PasswordUtil.verify("Test@123".toCharArray(),one)); assertFalse(PasswordUtil.verify("wrong".toCharArray(),one)); assertFalse(PasswordUtil.verify("x".toCharArray(),"corrupt"));
    }
    @Test void parsesBreedListAndRejectsBadJson() throws Exception {
        BreedApiService api=new BreedApiService();
        assertEquals(java.util.List.of("hound","hound/afghan","poodle"),api.parseBreeds("{\"status\":\"success\",\"message\":{\"poodle\":[],\"hound\":[\"afghan\"]}}"));
        assertThrows(java.io.IOException.class,() -> api.parseBreeds("not json"));
        assertThrows(java.io.IOException.class,() -> api.image("../../invalid"));
    }
}
