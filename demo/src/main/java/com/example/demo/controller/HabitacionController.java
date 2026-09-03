package com.example.demo.controller;

import com.example.demo.entitys.EstadoHabitacion;
import com.example.demo.entitys.Habitacion;
import com.example.demo.service.HabitacionService;
import com.example.demo.service.TipoHabitacionService;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CAPA DE CONTROLADOR: CRUD de habitaciones del portal de administrador.
 *
 * El controlador no valida datos: llama al servicio y decide la pantalla según
 * la excepción que este lance.
 *
 * - NoSuchElementException   -> la habitación (o su tipo) no existe: se vuelve al listado.
 * - IllegalArgumentException -> el formulario trae datos inválidos: se vuelve al formulario.
 */
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
        try {
            habitacionService.crear(habitacion);
            return "redirect:/admin/habitaciones/read";
        } catch (IllegalArgumentException datosInvalidos) {
            prepararFormulario(model, habitacion, "Create room", "/admin/habitaciones/create");
            model.addAttribute("error", datosInvalidos.getMessage());
            return "habitaciones/formulario";
        }
    }

    // Full URL: http://localhost:8080/admin/habitaciones/read/{numero}
    @GetMapping("/read/{numero}")
    public String verDetalle(@PathVariable int numero, Model model, RedirectAttributes redireccion) {
        try {
            Habitacion habitacion = habitacionService.buscarPorNumero(numero);
            model.addAttribute("habitacion", habitacion);
            model.addAttribute("tipo", tipoHabitacionService.buscarPorId(habitacion.getIdTipo()));
            return "habitaciones/detalle";
        } catch (NoSuchElementException habitacionInexistente) {
            redireccion.addFlashAttribute("error", habitacionInexistente.getMessage());
            return "redirect:/admin/habitaciones/read";
        }
    }

    // Full URL: http://localhost:8080/admin/habitaciones/update/{numero}
    @GetMapping("/update/{numero}")
    public String mostrarFormularioEdicion(@PathVariable int numero, Model model, RedirectAttributes redireccion) {
        try {
            Habitacion habitacion = habitacionService.buscarPorNumero(numero);
            prepararFormulario(model, habitacion, "Update room", "/admin/habitaciones/update/" + numero);
            return "habitaciones/formulario";
        } catch (NoSuchElementException habitacionInexistente) {
            redireccion.addFlashAttribute("error", habitacionInexistente.getMessage());
            return "redirect:/admin/habitaciones/read";
        }
    }

    // Full URL: http://localhost:8080/admin/habitaciones/update/{numero}
    @PostMapping("/update/{numero}")
    public String actualizar(@PathVariable int numero,
                             @ModelAttribute Habitacion habitacion,
                             Model model,
                             RedirectAttributes redireccion) {
        try {
            habitacionService.actualizar(numero, habitacion);
            return "redirect:/admin/habitaciones/read";
        } catch (NoSuchElementException habitacionInexistente) {
            redireccion.addFlashAttribute("error", habitacionInexistente.getMessage());
            return "redirect:/admin/habitaciones/read";
        } catch (IllegalArgumentException datosInvalidos) {
            prepararFormulario(model, habitacion, "Update room", "/admin/habitaciones/update/" + numero);
            model.addAttribute("error", datosInvalidos.getMessage());
            return "habitaciones/formulario";
        }
    }

    // Full URL: http://localhost:8080/admin/habitaciones/delete/{numero}
    @PostMapping("/delete/{numero}")
    public String eliminar(@PathVariable int numero, RedirectAttributes redireccion) {
        try {
            habitacionService.eliminar(numero);
        } catch (NoSuchElementException habitacionInexistente) {
            redireccion.addFlashAttribute("error", habitacionInexistente.getMessage());
        }

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
