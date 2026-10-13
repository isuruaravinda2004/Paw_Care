package com.pawcare.exception;
/** Checked exception: the view catches this and displays a useful message. */
public class ValidationException extends Exception {
    public ValidationException(String message) { super(message); }
}
