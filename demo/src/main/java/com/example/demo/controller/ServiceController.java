package com.example.demo.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.service.ServicioService;

/**
 * CAPA DE CONTROLADOR: recibe las peticiones de la pantalla de servicios,
 * le pide los datos a la capa de servicio y los envía a la vista con el modelo.
 * Si el servicio pedido no existe, la capa de servicio lanza
 * NoSuchElementException y aquí se redirige a la carta de servicios.
 */
@Controller
@RequestMapping("/servicios")
public class ServiceController {

    @Autowired
    ServicioService service;

    // Full URL: http://localhost:8080/servicios/tarjetas
    @GetMapping("/tarjetas")
    public String listarServiciosCards(Model model) {
        model.addAttribute("servicios", service.listarServicios());
        model.addAttribute("viewMode", "cards");
        return "servicios/servicios-tarjetas";
    }

    // Full URL: http://localhost:8080/servicios/lista
    @GetMapping("/lista")
    public String listarServiciosList(Model model) {
        model.addAttribute("servicios", service.listarServicios());
        model.addAttribute("viewMode", "list");
        return "servicios/servicios";
    }

    // Full URL: http://localhost:8080/servicios/{nombreUrl}
    @GetMapping("/{nombreUrl}")
    public String especifico(@PathVariable("nombreUrl") String nombreUrl,
                             Model model,
                             RedirectAttributes redireccion) {
        try {
            model.addAttribute("servicio", service.getServiceByNombreUrl(nombreUrl));
            return "servicios/servicio_especifico";
        } catch (NoSuchElementException servicioInexistente) {
            redireccion.addFlashAttribute("error", servicioInexistente.getMessage());
            return "redirect:/servicios/tarjetas";
        }
    }
}
