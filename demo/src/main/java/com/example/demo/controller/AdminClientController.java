package com.example.demo.controller;

import com.example.demo.service.interfaces.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** Handles the administrator's client listing screen. */
@Controller
@RequestMapping("/admin/clients")
public class AdminClientController {

    @Autowired
    ClientService clientService;

    // Full URL: http://localhost:8080/admin/clients/read
    @GetMapping("/read")
    public String listClients(Model model, @RequestParam(required = false) Integer adminId, @RequestParam(required = false) Integer operatorId) {
        prepareNavigation(model, adminId, operatorId);
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
    public String deleteClient(@PathVariable Integer clientId, @RequestParam(required = false) Integer adminId, @RequestParam(required = false) Integer operatorId, Model model) {
        prepareNavigation(model, adminId, operatorId);
        clientService.deleteProfile(clientId);
        return "redirect:/admin/clients/read" + navigationQuery(adminId, operatorId);
    }
    // El identificador se conserva en la URL; no se guarda en sesion.
    private void prepareNavigation(Model model, Integer adminId, Integer operatorId) {
        if (adminId != null) {
            model.addAttribute("adminId", adminId);
            model.addAttribute("panelUrl", "/admin/panel/" + adminId);
            model.addAttribute("profileUrl", "/admins/read/" + adminId);
        } else if (operatorId != null) {
            model.addAttribute("operatorId", operatorId);
            model.addAttribute("panelUrl", "/operators/panel/" + operatorId);
            model.addAttribute("profileUrl", "/operators/read/" + operatorId);
        }
    }

    private String navigationQuery(Integer adminId, Integer operatorId) {
        if (adminId != null) return "?adminId=" + adminId;
        return operatorId == null ? "" : "?operatorId=" + operatorId;
    }
}
