package com.example.demo.controller;

import com.example.demo.entitys.TipoHabitacion;
import com.example.demo.service.TipoHabitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CAPA DE CONTROLADOR: CRUD de tipos de habitación del portal de administrador.
 *
 * A diferencia del cliente, que se crea a sí mismo, aquí es el administrador
 * quien crea, edita y elimina registros que le pertenecen a otros: por eso todas
 * las rutas cuelgan de /admin y sí existe una pantalla de creación además del
 * listado completo del catálogo.
 */
@Controller
@RequestMapping("/admin/tipos-habitacion")
public class TipoHabitacionController {

    @Autowired
    TipoHabitacionService tipoHabitacionService;

    /**
     * Listado del catálogo de tipos de habitación.
     * URL: http://localhost:8080/admin/tipos-habitacion/read
     */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/read
    @GetMapping("/read")
    public String listarTipos(Model model) {
        model.addAttribute("tipos", tipoHabitacionService.listarTipos());
        return "tipos-habitacion/lista";
    }

    /**
     * Muestra el formulario de creación vacío.
     * URL: http://localhost:8080/admin/tipos-habitacion/create
     */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/create
    @GetMapping("/create")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("tipo", new TipoHabitacion());
        model.addAttribute("titulo", "Nuevo tipo de habitación");
        model.addAttribute("accion", "/admin/tipos-habitacion/create");
        return "tipos-habitacion/formulario";
    }

    /** Crea el tipo de habitación que llenó el administrador. */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/create
    @PostMapping("/create")
    public String crear(@ModelAttribute TipoHabitacion tipo, Model model) {
        String error = tipoHabitacionService.crear(tipo);

        if (error != null) {
            model.addAttribute("tipo", tipo);
            model.addAttribute("titulo", "Nuevo tipo de habitación");
            model.addAttribute("accion", "/admin/tipos-habitacion/create");
            model.addAttribute("error", error);
            return "tipos-habitacion/formulario";
        }

        return "redirect:/admin/tipos-habitacion/read";
    }

    /**
     * Muestra el formulario con los datos actuales del tipo para modificarlos.
     * URL: http://localhost:8080/admin/tipos-habitacion/update/{nombre}
     */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/update/{nombre}
    @GetMapping("/update/{nombre}")
    public String mostrarFormularioEdicion(@PathVariable("nombre") String nombre, Model model) {
        TipoHabitacion tipo = tipoHabitacionService.buscarPorNombre(nombre);
        if (tipo == null) {
            return "redirect:/admin/tipos-habitacion/read";
        }

        model.addAttribute("tipo", tipo);
        model.addAttribute("titulo", "Editar tipo de habitación");
        model.addAttribute("accion", "/admin/tipos-habitacion/update/" + nombre);
        return "tipos-habitacion/formulario";
    }

    /**
     * Guarda los cambios. El nombre de la URL es el que tenía el tipo antes de
     * editarlo, porque el administrador puede estar cambiando justamente el nombre.
     */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/update/{nombre}
    @PostMapping("/update/{nombre}")
    public String actualizar(@PathVariable("nombre") String nombreActual,
                             @ModelAttribute TipoHabitacion tipo,
                             Model model) {
        String error = tipoHabitacionService.actualizar(nombreActual, tipo);

        if (error != null) {
            model.addAttribute("tipo", tipo);
            model.addAttribute("titulo", "Editar tipo de habitación");
            model.addAttribute("accion", "/admin/tipos-habitacion/update/" + nombreActual);
            model.addAttribute("error", error);
            return "tipos-habitacion/formulario";
        }

        return "redirect:/admin/tipos-habitacion/read";
    }

    /**
     * Elimina un tipo de habitación del catálogo.
     * Se usa POST y no GET porque es una acción que modifica datos.
     */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/delete/{nombre}
    @PostMapping("/delete/{nombre}")
    public String eliminar(@PathVariable("nombre") String nombre) {
        tipoHabitacionService.eliminar(nombre);
        return "redirect:/admin/tipos-habitacion/read";
    }
}
