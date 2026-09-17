package com.example.demo.errors;

/**
 * Datos de operario que no cumplen las reglas del negocio.
 *
 * Además de las validaciones de texto incluye ADMIN_REQUIRED, porque la columna
 * admin_id de la tabla OPERATOR es NOT NULL: todo operario tiene que quedar a
 * cargo de un administrador.
 */
public class InvalidOperatorDataException extends RuntimeException {

    public enum Reason {
        NAME_REQUIRED,
        NAME_TOO_LONG,
        LAST_NAME_REQUIRED,
        LAST_NAME_TOO_LONG,
        EMAIL_INVALID,
        EMAIL_ALREADY_REGISTERED,
        ADMIN_REQUIRED
    }

    public InvalidOperatorDataException(Reason reason, String value) {
        super(buildMessage(reason, value));
    }

    private static String buildMessage(Reason reason, String value) {
        return switch (reason) {
            case NAME_REQUIRED -> "Enter the operator's first name.";
            case NAME_TOO_LONG -> "The first name cannot exceed 50 characters.";
            case LAST_NAME_REQUIRED -> "Enter the operator's last name.";
            case LAST_NAME_TOO_LONG -> "The last name cannot exceed 50 characters.";
            case EMAIL_INVALID -> "Enter a valid email address with at most 80 characters.";
            case EMAIL_ALREADY_REGISTERED -> "An operator is already registered with the email " + value + ".";
            case ADMIN_REQUIRED -> "Select the administrator in charge of this operator.";
        };
    }
}
