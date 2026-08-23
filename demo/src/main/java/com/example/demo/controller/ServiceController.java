package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entitys.Servicio;
import com.example.demo.service.ServicioService;


@Controller
@RequestMapping("/servicio")
public class ServiceController {

    @Autowired
    ServicioService service;

    // Full URL: http://localhost:8080/servicio/{nombreUrl}
    @GetMapping("/{nombreUrl}")
    public String especifico(@PathVariable("nombreUrl") String nombreUrl, Model model) {

        Servicio servicio = service.getServiceByNombreUrl(nombreUrl);

        model.addAttribute("servicio", servicio);
        
        return "servicio_especifico";
    }
    
}
