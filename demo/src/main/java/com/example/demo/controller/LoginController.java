package com.example.demo.controller;

import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Handles the intentionally simple login flow used by this prototype. */
@Controller
public class LoginController {

    @Autowired
    ClienteService clienteService;

    // Full URL: http://localhost:8080/login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login/login";
    }

    // Full URL: http://localhost:8080/login
    @PostMapping("/login")
    public String autenticar(@RequestParam String usuario, @RequestParam String password, Model model) {
        if ("admin".equals(usuario) && "admin".equals(password)) {
            return "redirect:/admin/panel";
        }

        Cliente cliente = clienteService.buscarPorCorreo(usuario);
        if (cliente != null && cliente.getPassword().equals(password)) {
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
