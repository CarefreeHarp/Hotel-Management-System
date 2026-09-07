package com.example.demo.errors;

/**
 * Se lanza cuando el formulario del tipo de habitación trae un dato que no
 * cumple las reglas del negocio.
 *
 * El motivo se pasa como enum en vez de escribir el texto en cada throw, para
 * que todos los mensajes del tipo de habitación vivan en un solo lugar.
 */
public class InvalidRoomTypeDataException extends RuntimeException {

    public enum Reason {
        NAME_REQUIRED,
        NAME_TOO_LONG,
        NAME_ALREADY_EXISTS,
        DESCRIPTION_REQUIRED,
        DESCRIPTION_TOO_LONG,
        NIGHTLY_PRICE_INVALID,
        MAX_CAPACITY_INVALID
    }

    public InvalidRoomTypeDataException(Reason reason, Object value) {
        super(buildMessage(reason, value));
    }

    private static String buildMessage(Reason reason, Object value) {
        return switch (reason) {
            case NAME_REQUIRED -> "Enter a name for the room type.";
            case NAME_TOO_LONG -> "The room type name cannot exceed 50 characters.";
            case NAME_ALREADY_EXISTS -> "A room type named " + value + " already exists.";
            case DESCRIPTION_REQUIRED -> "Enter a description for the room type.";
            case DESCRIPTION_TOO_LONG -> "The description cannot exceed 500 characters.";
            case NIGHTLY_PRICE_INVALID -> "The nightly price cannot be negative; received: " + value + ".";
            case MAX_CAPACITY_INVALID -> "Maximum capacity must be at least one guest; received: " + value + ".";
        };
    }
}
