package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

/** Thymeleaf añade el token automáticamente a los formularios con th:action. */
@Component("requestDataValueProcessor")
public class PortalFormTokens implements RequestDataValueProcessor {
    public static String token(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("formToken");
        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute("formToken", token);
        }
        return token;
    }

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) { return action; }
    @Override
    public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) { return value; }
    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        return Map.of("_formToken", token(request));
    }
    @Override
    public String processUrl(HttpServletRequest request, String url) { return url; }
}
