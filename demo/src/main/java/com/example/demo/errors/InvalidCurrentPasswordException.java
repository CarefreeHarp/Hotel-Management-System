package com.example.demo.errors;

public class InvalidCurrentPasswordException extends RuntimeException {

    public InvalidCurrentPasswordException(String email) {
        super("The current password does not match the guest profile with the email " + email + ".");
    }
}
