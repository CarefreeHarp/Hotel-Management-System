package com.example.demo.controller;

import com.example.demo.service.ClientService;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Handles the administrator's client listing screen. */
@Controller
@RequestMapping("/admin/clientes")
public class AdminClientController {

    @Autowired
    ClientService clientService;

    // Full URL: http://localhost:8080/admin/clientes/read
    @GetMapping("/read")
    public String listClients(Model model) {
        model.addAttribute("clientes", clientService.listClients());
        return "clientes/lista";
    }

    /**
     * Elimina la cuenta de un cliente desde el listado del administrador.
     * Es la misma operación de negocio que el borrado del cliente, pero como
     * aquí quien borra es el administrador se vuelve al listado y no al login.
     */
    // Full URL: http://localhost:8080/admin/clientes/delete/{email}
    @PostMapping("/delete/{email}")
    public String deleteClient(@PathVariable("email") String email, RedirectAttributes redirectAttributes) {
        try {
            clientService.deleteProfile(email);
        } catch (NoSuchElementException profileNotFound) {
            redirectAttributes.addFlashAttribute("error", profileNotFound.getMessage());
        }

        return "redirect:/admin/clientes/read";
    }
}
