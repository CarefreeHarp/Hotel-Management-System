package com.example.demo.config;

import com.example.demo.service.AuthenticatedAccount;
import com.example.demo.service.interfaces.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import static com.example.demo.service.AuthenticatedAccount.Role.*;

/** Protege el panel también al escribir sus URLs directamente. */
@Component
public class PortalAccessInterceptor implements HandlerInterceptor {
    private final LoginService loginService;
    public PortalAccessInterceptor(LoginService loginService) { this.loginService = loginService; }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Usa la ruta que Spring resolvió, sin parámetros de matriz ni codificaciones.
        String path = String.valueOf(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        if (!java.util.Set.of("GET", "HEAD", "OPTIONS").contains(request.getMethod())) {
            HttpSession current = request.getSession(false);
            String expected = current == null ? null : (String) current.getAttribute("formToken");
            if (expected == null || !expected.equals(request.getParameter("_formToken"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Reload the form and try again.");
                return false;
            }
        }
        if (path.equals("/login") || path.equals("/staff/login") || path.equals("/logout")) return true;
        if (path.equals("/clients/create")) return true;
        HttpSession session = request.getSession(false);
        AuthenticatedAccount account = session == null ? null : (AuthenticatedAccount) session.getAttribute("account");
        if (account == null || !loginService.exists(account)) {
            if (session != null) session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        boolean staff = account.role() != CLIENT;
        boolean allowed = staff;
        if (path.startsWith("/admin/administrators") || path.startsWith("/admin/operators") || path.startsWith("/admins/")) {
            allowed = account.role() == ADMINISTRATOR;
        } else if (path.startsWith("/operators/")) {
            allowed = account.role() == OPERATOR;
        } else if (path.startsWith("/clients/")) {
            Object variables = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Object clientId = variables instanceof java.util.Map<?, ?> map ? map.get("clientId") : null;
            allowed = staff || account.id().toString().equals(clientId);
        }
        if (!allowed) response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return allowed;
    }
}
