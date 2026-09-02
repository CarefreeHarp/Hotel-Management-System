package com.example.demo.controller;

import com.example.demo.entitys.Cliente;
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
 * lo que este responde solo decide a qué pantalla se redirige.
 */
@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    // Full URL: http://localhost:8080/login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login/login";
    }

    // Full URL: http://localhost:8080/login
    @PostMapping("/login")
    public String autenticar(@RequestParam String usuario, @RequestParam String password, Model model) {
        if (loginService.esAdministrador(usuario, password)) {
            return "redirect:/admin/panel";
        }

        Cliente cliente = loginService.autenticarCliente(usuario, password);
        if (cliente != null) {
            return "redirect:/clientes/read/" + cliente.getCorreo();
        }

        model.addAttribute("error", "Incorrect username or password.");
        return "login/login";
    }

    // Full URL: http://localhost:8080/admin/panel
    @GetMapping("/admin/panel")
    public String mostrarPanelAdmin() {
        return "admin/panel";
    }
}
