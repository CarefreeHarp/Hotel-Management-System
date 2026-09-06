package com.example.demo.errors;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String email) {
        super("No guest profile is registered with the email " + email + ".");
    }
}
