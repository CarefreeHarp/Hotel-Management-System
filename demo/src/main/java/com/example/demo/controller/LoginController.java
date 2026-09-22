package com.example.demo.controller;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Client;
import com.example.demo.entities.Operator;
import com.example.demo.service.interfaces.LoginService;
import com.example.demo.security.SessionAccess;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping({"/login", "/staff/login"})
    public String showLogin(HttpServletRequest request, Model model) {
        model.addAttribute("staffLogin", request.getServletPath().equals("/staff/login"));
        return "login/login";
    }

    @PostMapping({"/login", "/staff/login"})
    public String authenticate(@RequestParam String user, @RequestParam String password,
                               Model model, HttpServletRequest request) {
        boolean staffLogin = request.getServletPath().equals("/staff/login");
        if (staffLogin) {
            return authenticateStaff(user, password, model, request);
        }
        return authenticateClient(user, password, model, request);
    }

    private String authenticateStaff(String user, String password, Model model, HttpServletRequest request) {
        Administrator administrator = loginService.authenticateAdministrator(user, password);
        Operator operator = loginService.authenticateOperator(user, password);

        if ((administrator == null && operator == null) || (administrator != null && operator != null)) {
            model.addAttribute("staffLogin", true);
            model.addAttribute("error", "Incorrect email or password.");
            return "login/login";
        }
        if (administrator != null) {
            HttpSession session = openFreshSession(request);
            session.setAttribute(SessionAccess.ROLE, SessionAccess.ADMIN);
            session.setAttribute("adminId", administrator.getAdminId());
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/panel/" + administrator.getAdminId();
        }
        HttpSession session = openFreshSession(request);
        session.setAttribute(SessionAccess.ROLE, SessionAccess.OPERATOR);
        session.setAttribute("operatorId", operator.getOperatorId());
        return "redirect:/operators/panel/" + operator.getOperatorId();
    }

    private String authenticateClient(String user, String password, Model model, HttpServletRequest request) {
        Client client = loginService.authenticateClient(user, password);
        if (client == null) {
            model.addAttribute("staffLogin", false);
            model.addAttribute("error", "Incorrect email or password.");
            return "login/login";
        }

        // El destino pendiente debe rescatarse ANTES de invalidar la sesion.
        HttpSession previous = request.getSession(false);
        Object destination = previous == null ? null : previous.getAttribute("pendingClientDestination");

        HttpSession session = openFreshSession(request);
        session.setAttribute(SessionAccess.ROLE, SessionAccess.CLIENT);
        session.setAttribute("clientId", client.getClientId());
        if ("/reservation/book".equals(destination)) {
            return "redirect:/reservation/book";
        }
        return "redirect:/clients/read/" + client.getClientId();
    }

    /**
     * Cierra la sesion anterior y abre una nueva.
     *
     * Rotar el identificador en cada login es lo que impide que alguien fije un
     * JSESSIONID de antemano y herede la cuenta ajena (session fixation). Como
     * invalidate() borra todos los atributos, lo que deba sobrevivir al cambio
     * hay que rescatarlo antes y reescribirlo despues.
     */
    private HttpSession openFreshSession(HttpServletRequest request) {
        HttpSession previous = request.getSession(false);
        if (previous != null) {
            previous.invalidate();
        }
        return request.getSession(true);
    }

}
