package com.example.demo.errors;

public class InvalidClientDataException extends RuntimeException {

    public enum Reason {
        NAME_REQUIRED,
        NAME_TOO_LONG,
        LAST_NAME_REQUIRED,
        LAST_NAME_TOO_LONG,
        NATIONAL_ID_INVALID,
        PHONE_INVALID,
        EMAIL_INVALID,
        PASSWORD_TOO_SHORT,
        EMAIL_ALREADY_REGISTERED,
        NATIONAL_ID_ALREADY_REGISTERED
    }

    public InvalidClientDataException(Reason reason, String value) {
        super(buildMessage(reason, value));
    }

    private static String buildMessage(Reason reason, String value) {
        return switch (reason) {
            case NAME_REQUIRED -> "Enter the guest's first name.";
            case NAME_TOO_LONG -> "The first name cannot exceed 50 characters.";
            case LAST_NAME_REQUIRED -> "Enter the guest's last name.";
            case LAST_NAME_TOO_LONG -> "The last name cannot exceed 50 characters.";
            case NATIONAL_ID_INVALID -> "The national ID must contain 6 to 15 digits.";
            case PHONE_INVALID -> "The phone number must contain 7 to 15 digits.";
            case EMAIL_INVALID -> "Enter a valid email address with at most 80 characters.";
            case PASSWORD_TOO_SHORT -> "The password must contain at least 8 characters.";
            case EMAIL_ALREADY_REGISTERED -> "A guest profile is already registered with the email " + value + ".";
            case NATIONAL_ID_ALREADY_REGISTERED -> "A guest profile is already registered with the national ID " + value + ".";
        };
    }
}
