package com.example.demo.errors;

/** Representa un dato inválido recibido desde el formulario de pago. */
public class InvalidPaymentDataException extends RuntimeException {

    public enum Reason {
        PAYMENT_AMOUNT_REQUIRED,
        PAYMENT_AMOUNT_INVALID,
        PAYMENT_AMOUNT_EXCEEDS_TOTAL,
        CARDHOLDER_NAME_REQUIRED,
        CARD_NUMBER_INVALID,
        EXPIRY_DATE_INVALID,
        SECURITY_CODE_INVALID
    }

    public InvalidPaymentDataException(Reason reason, Object value) {
        super(buildMessage(reason, value));
    }

    private static String buildMessage(Reason reason, Object value) {
        return switch (reason) {
            case PAYMENT_AMOUNT_REQUIRED -> "Enter the amount you want to pay today.";
            case PAYMENT_AMOUNT_INVALID -> "The payment amount must be greater than zero.";
            case PAYMENT_AMOUNT_EXCEEDS_TOTAL -> "The payment amount cannot exceed the reservation total.";
            case CARDHOLDER_NAME_REQUIRED -> "Enter the name shown on the card.";
            case CARD_NUMBER_INVALID -> "Enter a valid 16-digit card number.";
            case EXPIRY_DATE_INVALID -> "Enter the expiry date in MM/YY format.";
            case SECURITY_CODE_INVALID -> "Enter a valid 3-digit security code.";
        };
    }
}
