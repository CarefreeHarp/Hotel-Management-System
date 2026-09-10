package com.example.demo.controller;

import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.UUID;

/** Handles the administrator's client listing screen. */
@Controller
@RequestMapping("/admin/clients")
public class AdminClientController {

    @Autowired
    ClientService clientService;

    // Full URL: http://localhost:8080/admin/clients/read
    @GetMapping("/read")
    public String listClients(Model model) {
        model.addAttribute("clientes", clientService.listClients());
        return "clients/list";
    }

    /**
     * Elimina la cuenta de un cliente desde el listado del administrador.
     * Es la misma operación de negocio que el borrado del cliente, pero como
     * aquí quien borra es el administrador se vuelve al listado y no al login.
     */
    // Full URL: http://localhost:8080/admin/clients/delete/{clientId}
    @PostMapping("/delete/{clientId}")
    public String deleteClient(@PathVariable UUID clientId) {
        clientService.deleteProfile(clientId);
        return "redirect:/admin/clients/read";
    }
}
