package com.example.demo.controller;

import com.example.demo.entitys.EstadoHabitacion;
import com.example.demo.entitys.Habitacion;
import com.example.demo.entitys.TipoHabitacion;
import com.example.demo.service.HabitacionService;
import com.example.demo.service.TipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** Controller for the administrator's room CRUD screens. */
@Controller
@RequestMapping("/admin/habitaciones")
public class HabitacionController {

    @Autowired
    HabitacionService habitacionService;

    @Autowired
    TipoHabitacionService tipoHabitacionService;

    // Full URL: http://localhost:8080/admin/habitaciones/read
    @GetMapping("/read")
    public String listarHabitaciones(Model model) {
        model.addAttribute("habitaciones", habitacionService.listarHabitaciones());
        return "habitaciones/lista";
    }

    // Full URL: http://localhost:8080/admin/habitaciones/create
    @GetMapping("/create")
    public String mostrarFormularioCreacion(Model model) {
        prepararFormulario(model, new Habitacion(), "Create room", "/admin/habitaciones/create");
        return "habitaciones/formulario";
    }

    // Full URL: http://localhost:8080/admin/habitaciones/create
    @PostMapping("/create")
    public String crear(@ModelAttribute Habitacion habitacion, Model model) {
        String error = habitacionService.crear(habitacion);
        if (error != null) {
            prepararFormulario(model, habitacion, "Create room", "/admin/habitaciones/create");
            model.addAttribute("error", error);
            return "habitaciones/formulario";
        }

        return "redirect:/admin/habitaciones/read";
    }

    // Full URL: http://localhost:8080/admin/habitaciones/read/{numero}
    @GetMapping("/read/{numero}")
    public String verDetalle(@PathVariable int numero, Model model) {
        Habitacion habitacion = habitacionService.buscarPorNumero(numero);
        if (habitacion == null) {
            return "redirect:/admin/habitaciones/read";
        }

        TipoHabitacion tipo = tipoHabitacionService.listarTipos().stream()
                .filter(tipoHabitacion -> tipoHabitacion.getIdTipo() == habitacion.getIdTipo())
                .findFirst()
                .orElse(null);
        model.addAttribute("habitacion", habitacion);
        model.addAttribute("tipo", tipo);
        return "habitaciones/detalle";
    }

    // Full URL: http://localhost:8080/admin/habitaciones/update/{numero}
    @GetMapping("/update/{numero}")
    public String mostrarFormularioEdicion(@PathVariable int numero, Model model) {
        Habitacion habitacion = habitacionService.buscarPorNumero(numero);
        if (habitacion == null) {
            return "redirect:/admin/habitaciones/read";
        }

        prepararFormulario(model, habitacion, "Update room", "/admin/habitaciones/update/" + numero);
        return "habitaciones/formulario";
    }

    // Full URL: http://localhost:8080/admin/habitaciones/update/{numero}
    @PostMapping("/update/{numero}")
    public String actualizar(@PathVariable int numero, @ModelAttribute Habitacion habitacion, Model model) {
        String error = habitacionService.actualizar(numero, habitacion);
        if (error != null) {
            prepararFormulario(model, habitacion, "Update room", "/admin/habitaciones/update/" + numero);
            model.addAttribute("error", error);
            return "habitaciones/formulario";
        }

        return "redirect:/admin/habitaciones/read";
    }

    // Full URL: http://localhost:8080/admin/habitaciones/delete/{numero}
    @PostMapping("/delete/{numero}")
    public String eliminar(@PathVariable int numero) {
        habitacionService.eliminar(numero);
        return "redirect:/admin/habitaciones/read";
    }

    private void prepararFormulario(Model model, Habitacion habitacion, String titulo, String accion) {
        model.addAttribute("habitacion", habitacion);
        model.addAttribute("tipos", tipoHabitacionService.listarTipos());
        model.addAttribute("estados", EstadoHabitacion.values());
        model.addAttribute("titulo", titulo);
        model.addAttribute("accion", accion);
    }
}
