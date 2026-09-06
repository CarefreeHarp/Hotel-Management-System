package com.example.demo.controller;

import com.example.demo.entities.Client;
import com.example.demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * CAPA DE CONTROLADOR: pantalla de acceso al portal.
 *
 * El controlador no comprueba credenciales: se las pasa al LoginService y con
 * lo que este responde solo decide a qué pantalla se redirige. Si las
 * credenciales no sirven el servicio lanza SecurityException y aquí solo se
 * atrapa para volver al login con el mensaje que el servicio escribió.
 */
@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    // Full URL: http://localhost:8080/login
    @GetMapping("/login")
    public String showLogin() {
        return "login/login";
    }

    // Full URL: http://localhost:8080/login
    @PostMapping("/login")
    public String authenticate(@RequestParam String user, @RequestParam String password, Model model) {
        if (loginService.isAdministrator(user, password)) {
            return "redirect:/admin/panel";
        }

        try {
            Client client = loginService.authenticateClient(user, password);
            return "redirect:/clientes/read/" + client.getEmail();
        } catch (SecurityException credencialesInvalidas) {
            model.addAttribute("error", credencialesInvalidas.getMessage());
            return "login/login";
        }
    }

    // Full URL: http://localhost:8080/admin/panel
    @GetMapping("/admin/panel")
    public String showPanelAdmin() {
        return "admin/panel";
    }
}
