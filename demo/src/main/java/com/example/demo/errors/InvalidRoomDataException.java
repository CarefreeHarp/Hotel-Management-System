package com.example.demo.errors;

public class InvalidRoomDataException extends RuntimeException {

    public enum Reason {
        ROOM_NUMBER_MUST_BE_POSITIVE,
        ROOM_NUMBER_ALREADY_EXISTS,
        FLOOR_CANNOT_BE_NEGATIVE,
        STATUS_REQUIRED,
        ROOM_TYPE_REQUIRED,
        MAIN_PHOTO_REQUIRED
    }

    public InvalidRoomDataException(Reason reason, Object value) {
        super(buildMessage(reason, value));
    }

    private static String buildMessage(Reason reason, Object value) {
        return switch (reason) {
            case ROOM_NUMBER_MUST_BE_POSITIVE -> "The room number must be greater than zero; received: " + value + ".";
            case ROOM_NUMBER_ALREADY_EXISTS -> "A room with the number " + value + " already exists.";
            case FLOOR_CANNOT_BE_NEGATIVE -> "The floor cannot be negative; received: " + value + ".";
            case STATUS_REQUIRED -> "Select a room status for room " + value + ".";
            case ROOM_TYPE_REQUIRED -> "Select an existing room type for room " + value + ".";
            case MAIN_PHOTO_REQUIRED -> "Enter a main photo for room " + value + ".";
        };
    }
}
