package com.example.demo.errors;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(int number) {
        super("The room " + number + " does not exist.");
    }
}
