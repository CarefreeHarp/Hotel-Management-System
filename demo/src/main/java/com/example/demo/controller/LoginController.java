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
        Administrator administrator = loginService.authenticateAdministrator(user, password);
        Operator operator = loginService.authenticateOperator(user, password);
        Client client = loginService.authenticateClient(user, password);

        int matches = 0;
        if (administrator != null) matches++;
        if (operator != null) matches++;
        if (client != null) matches++;
        if (matches != 1) {
            model.addAttribute("staffLogin", staffLogin);
            model.addAttribute("error", "Incorrect email or password.");
            return "login/login";
        }
        if (staffLogin && client != null) {
            model.addAttribute("staffLogin", true);
            model.addAttribute("error", "Use guest sign in for your client account.");
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
        if (administrator != null) {
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/panel/" + administrator.getAdminId();
        }
        if (operator != null) {
            return "redirect:/operators/panel/" + operator.getOperatorId();
        }
        session.setAttribute("clientId", client.getClientId());
        if ("/reservation/book".equals(destination)) {
            return "redirect:/reservation/book";
        }
        return "redirect:/clients/read/" + client.getClientId();
    }


}
