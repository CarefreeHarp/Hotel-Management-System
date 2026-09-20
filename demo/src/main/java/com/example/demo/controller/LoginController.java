package com.example.demo.controller;

import com.example.demo.entities.Administrator;
import com.example.demo.entities.Client;
import com.example.demo.entities.Operator;
import com.example.demo.service.interfaces.LoginService;
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
            return authenticateStaff(user, password, model);
        }
        return authenticateClient(user, password, model, request);
    }

    private String authenticateStaff(String user, String password, Model model) {
        Administrator administrator = loginService.authenticateAdministrator(user, password);
        Operator operator = loginService.authenticateOperator(user, password);

        if ((administrator == null && operator == null) || (administrator != null && operator != null)) {
            model.addAttribute("staffLogin", true);
            model.addAttribute("error", "Incorrect email or password.");
            return "login/login";
        }
        if (administrator != null) {
            return "redirect:/admin/panel/" + administrator.getAdminId();
        }
        return "redirect:/operators/panel/" + operator.getOperatorId();
    }

    private String authenticateClient(String user, String password, Model model, HttpServletRequest request) {
        Client client = loginService.authenticateClient(user, password);
        if (client == null) {
            model.addAttribute("staffLogin", false);
            model.addAttribute("error", "Incorrect email or password.");
            return "login/login";
        }

        // Se abre una sesión nueva para no mezclar cuentas al cambiar de usuario.
        HttpSession previous = request.getSession(false);
        Object destination = null;
        if (previous != null) {
            destination = previous.getAttribute("pendingClientDestination");
            previous.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("clientId", client.getClientId());
        if ("/reservation/book".equals(destination)) {
            return "redirect:/reservation/book";
        }
        return "redirect:/clients/read/" + client.getClientId();
    }

}
