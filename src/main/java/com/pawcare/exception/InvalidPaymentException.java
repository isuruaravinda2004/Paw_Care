package com.pawcare.exception;
/** Checked exception: the view catches this and displays a useful message. */
public class InvalidPaymentException extends ValidationException {
    public InvalidPaymentException(String message) { super(message); }
}
