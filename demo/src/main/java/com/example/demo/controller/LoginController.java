package com.example.demo.controller;

import com.example.demo.service.AuthenticatedAccount;
import com.example.demo.service.interfaces.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static com.example.demo.service.AuthenticatedAccount.Role.*;

@Controller
public class LoginController {
    private final LoginService loginService;
    public LoginController(LoginService loginService) { this.loginService = loginService; }

    @GetMapping({"/login", "/staff/login"})
    public String showLogin(HttpServletRequest request, Model model) {
        model.addAttribute("staffLogin", request.getRequestURI().equals(request.getContextPath() + "/staff/login"));
        return "login/login";
    }

    @PostMapping({"/login", "/staff/login"})
    public String authenticate(@RequestParam String user, @RequestParam String password,
                               Model model, HttpServletRequest request) {
        boolean staffLogin = request.getRequestURI().equals(request.getContextPath() + "/staff/login");
        try {
            AuthenticatedAccount account = loginService.authenticate(user, password);
            if (staffLogin && account.role() == CLIENT) {
                throw new SecurityException("Use guest sign in for your client account.");
            }
            HttpSession previous = request.getSession(false);
            Object destination = previous == null ? null : previous.getAttribute("pendingClientDestination");
            if (previous != null) previous.invalidate();
            HttpSession session = request.getSession(true);
            session.setAttribute("account", account);
            session.setAttribute("isAdmin", account.role() == ADMINISTRATOR);
            switch (account.role()) {
                case CLIENT -> session.setAttribute("clientId", account.id());
                case OPERATOR -> session.setAttribute("operatorId", account.id());
                case ADMINISTRATOR -> session.setAttribute("adminId", account.id());
            }
            // Conserva el regreso al flujo de reservas iniciado por un cliente.
            if (account.role() == CLIENT && "/reservation/book".equals(destination)) {
                return "redirect:/reservation/book";
            }
            return "redirect:" + account.profilePath();
        } catch (SecurityException exception) {
            model.addAttribute("staffLogin", staffLogin);
            model.addAttribute("error", exception.getMessage());
            return "login/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/admin/panel")
    public String showPanelAdmin() { return "admin/panel"; }
}
