package com.example.demo.service;

/** Identidad autenticada, sin contraseñas en la sesión. */
public record AuthenticatedAccount(Integer id, Role role) {
    public enum Role { CLIENT, OPERATOR, ADMINISTRATOR }
    public String profilePath() {
        return switch (role) {
            case CLIENT -> "/clients/read/" + id;
            case OPERATOR -> "/operators/read/" + id;
            case ADMINISTRATOR -> "/admins/read/" + id;
        };
    }
}
