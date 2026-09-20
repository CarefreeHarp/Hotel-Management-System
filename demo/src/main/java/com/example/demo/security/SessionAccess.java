package com.example.demo.security;

import jakarta.servlet.http.HttpSession;

/**
 * Lectura de la identidad guardada en sesion y reglas de acceso a las
 * pantallas de consulta.
 *
 * La sesion la escribe unicamente LoginController; aqui solo se lee. El
 * identificador que viaja en la URL nunca se toma como prueba de identidad:
 * la URL dice QUE se quiere ver, la sesion dice QUIEN lo pide.
 */
public final class SessionAccess {

    public static final String ROLE = "role";
    public static final String ADMIN = "ADMIN";
    public static final String OPERATOR = "OPERATOR";
    public static final String CLIENT = "CLIENT";

    private SessionAccess() {
    }

    /** Rol guardado al iniciar sesion, o null si nadie se ha autenticado. */
    public static String role(HttpSession session) {
        Object role = session.getAttribute(ROLE);
        return role instanceof String ? (String) role : null;
    }

    public static boolean isAdmin(HttpSession session) {
        return ADMIN.equals(role(session));
    }

    public static boolean isOperator(HttpSession session) {
        return OPERATOR.equals(role(session));
    }

    public static boolean isClient(HttpSession session) {
        return CLIENT.equals(role(session));
    }

    /** Administradores y operarios comparten las pantallas de gestion. */
    public static boolean isStaff(HttpSession session) {
        return isAdmin(session) || isOperator(session);
    }

    /** Solo el propio cliente: un id ajeno en la URL no basta. */
    public static boolean isSelfClient(HttpSession session, Integer clientId) {
        Object own = session.getAttribute("clientId");
        return own instanceof Integer && own.equals(clientId);
    }

    /**
     * Destino de quien no puede entrar: cada rol vuelve a su propia pantalla y
     * el anonimo al login. No se distingue "no existe" de "no puedes verlo",
     * para no confirmar que el recurso existe.
     */
    public static String denied(HttpSession session) {
        String role = role(session);
        if (ADMIN.equals(role)) {
            return "redirect:/admin/panel/" + session.getAttribute("adminId");
        }
        if (OPERATOR.equals(role)) {
            return "redirect:/operators/panel/" + session.getAttribute("operatorId");
        }
        if (CLIENT.equals(role)) {
            return "redirect:/clients/read/" + session.getAttribute("clientId");
        }
        return "redirect:/login";
    }

    /** Las pantallas de staff devuelven al login de staff, no al publico. */
    public static String deniedStaff(HttpSession session) {
        return role(session) == null ? "redirect:/staff/login" : denied(session);
    }
}
