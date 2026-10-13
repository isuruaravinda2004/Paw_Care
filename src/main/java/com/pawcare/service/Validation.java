package com.pawcare.service;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.*;
import com.pawcare.exception.ValidationException;
/** Shared, readable validation methods. User mistakes are checked exceptions. */
public final class Validation {
    private Validation() { }
    public static String text(String value, String field, int maximum) throws ValidationException {
        if(value==null || value.trim().isEmpty()) throw new ValidationException(field + " is required.");
        if(value.trim().length()>maximum) throw new ValidationException(field + " must be at most " + maximum + " characters.");
        return value.trim();
    }
    public static int integer(String value, String field, int min, int max) throws ValidationException {
        try { int n=Integer.parseInt(value.trim()); if(n<min || n>max) throw new NumberFormatException(); return n; }
        catch(RuntimeException e) { throw new ValidationException(field + " must be a whole number from " + min + " to " + max + "."); }
    }
    public static BigDecimal money(String value, String field) throws ValidationException {
        try { BigDecimal n=new BigDecimal(value.trim());
            if(n.signum()<=0 || n.scale()>2 || n.compareTo(new BigDecimal("99999999.99"))>0) throw new NumberFormatException();
            return n.setScale(2);
        } catch(RuntimeException e) { throw new ValidationException(field+" must be positive, with at most 2 decimal places."); }
    }
    public static LocalDate date(String value) throws ValidationException {
        try { return LocalDate.parse(value.trim(),DateTimeFormatter.ISO_LOCAL_DATE); }
        catch(RuntimeException e) { throw new ValidationException("Enter a valid date as YYYY-MM-DD, for example 2026-10-15."); }
    }
    public static LocalTime time(String value) throws ValidationException {
        try { return LocalTime.parse(value.trim(),DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT)); }
        catch(RuntimeException e) { throw new ValidationException("Enter a valid 24-hour time as HH:mm, for example 14:30."); }
    }
    public static void choice(String value, String field, String... values) throws ValidationException {
        for(String allowed:values) if(allowed.equals(value)) return;
        throw new ValidationException("Select a valid " + field + ".");
    }
}
