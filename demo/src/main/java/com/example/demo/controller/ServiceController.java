package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entitys.Servicio;
import com.example.demo.service.ServicioService;

/**
 * CAPA DE CONTROLADOR: recibe las peticiones de la pantalla de servicios,
 * le pide los datos a la capa de servicio y los envía a la vista con el modelo.
 */
@Controller
@RequestMapping("/servicios")
public class ServiceController {

    @Autowired
    ServicioService service;

    // Full URL: http://localhost:8080/servicios or /servicios/cards
    @GetMapping({"", "/", "/cards"})
    public String listarServiciosCards(Model model) {
        model.addAttribute("servicios", service.listarServicios());
        model.addAttribute("viewMode", "cards");
        return "servicios";
    }

    // Full URL: http://localhost:8080/servicios/list
    @GetMapping("/list")
    public String listarServiciosList(Model model) {
        model.addAttribute("servicios", service.listarServicios());
        model.addAttribute("viewMode", "list");
        return "servicios";
    }

    // Full URL: http://localhost:8080/servicios/{nombreUrl}
    @GetMapping("/{nombreUrl}")
    public String especifico(@PathVariable("nombreUrl") String nombreUrl, Model model) {
        if ("cards".equalsIgnoreCase(nombreUrl)) {
            return listarServiciosCards(model);
        }
        if ("list".equalsIgnoreCase(nombreUrl)) {
            return listarServiciosList(model);
        }

        Servicio servicio = service.getServiceByNombreUrl(nombreUrl);
        if (servicio == null) {
            return "redirect:/servicios";
        }

        model.addAttribute("servicio", servicio);
        return "servicio_especifico";
    }
}

