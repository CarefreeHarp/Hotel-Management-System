package com.example.demo.controller;

import com.example.demo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** Handles the administrator's client listing screen. */
@Controller
@RequestMapping("/admin/clientes")
public class AdminClienteController {

    @Autowired
    ClienteService clienteService;

    // Full URL: http://localhost:8080/admin/clientes/read
    @GetMapping("/read")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "clientes/lista";
    }
}
