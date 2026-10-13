package com.pawcare.exception;
/** Checked exception: the view catches this and displays a useful message. */
public class InvalidAppointmentException extends ValidationException {
    public InvalidAppointmentException(String message) { super(message); }
}
