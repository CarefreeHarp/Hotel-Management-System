package com.example.demo.controller;

import com.example.demo.service.ClienteService;
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
public class AdminClienteController {

    @Autowired
    ClienteService clienteService;

    // Full URL: http://localhost:8080/admin/clientes/read
    @GetMapping("/read")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "clientes/lista";
    }

    /**
     * Elimina la cuenta de un cliente desde el listado del administrador.
     * Es la misma operación de negocio que el borrado del cliente, pero como
     * aquí quien borra es el administrador se vuelve al listado y no al login.
     */
    // Full URL: http://localhost:8080/admin/clientes/delete/{correo}
    @PostMapping("/delete/{correo}")
    public String eliminarCliente(@PathVariable("correo") String correo, RedirectAttributes redireccion) {
        try {
            clienteService.eliminarCuenta(correo);
        } catch (NoSuchElementException cuentaInexistente) {
            redireccion.addFlashAttribute("error", cuentaInexistente.getMessage());
        }

        return "redirect:/admin/clientes/read";
    }
}
