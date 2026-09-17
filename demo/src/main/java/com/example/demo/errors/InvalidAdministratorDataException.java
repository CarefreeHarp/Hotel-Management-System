package com.example.demo.errors;

/**
 * Datos de administrador que no cumplen las reglas del negocio.
 *
 * Sigue el mismo patrón que InvalidClientDataException: el servicio indica la
 * razón con un valor del enum y la clase arma el mensaje que verá el usuario,
 * de modo que los textos viven en un solo sitio.
 */
public class InvalidAdministratorDataException extends RuntimeException {

    public enum Reason {
        NAME_REQUIRED,
        NAME_TOO_LONG,
        EMAIL_INVALID,
        EMAIL_ALREADY_REGISTERED
    }

    public InvalidAdministratorDataException(Reason reason, String value) {
        super(buildMessage(reason, value));
    }

    private static String buildMessage(Reason reason, String value) {
        return switch (reason) {
            case NAME_REQUIRED -> "Enter the administrator's name.";
            case NAME_TOO_LONG -> "The name cannot exceed 50 characters.";
            case EMAIL_INVALID -> "Enter a valid email address with at most 80 characters.";
            case EMAIL_ALREADY_REGISTERED -> "An administrator is already registered with the email " + value + ".";
        };
    }
}
