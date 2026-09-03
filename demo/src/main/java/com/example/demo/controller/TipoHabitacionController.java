package com.example.demo.controller;

import com.example.demo.entitys.TipoHabitacion;
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
 * CAPA DE CONTROLADOR: CRUD de tipos de habitación del portal de administrador.
 *
 * A diferencia del cliente, que se crea a sí mismo, aquí es el administrador
 * quien crea, edita y elimina registros que le pertenecen a otros: por eso todas
 * las rutas cuelgan de /admin y sí existe una pantalla de creación además del
 * listado completo del catálogo.
 *
 * Las reglas del negocio las valida el servicio; el controlador solo atrapa la
 * excepción y decide la pantalla:
 *
 * - NoSuchElementException   -> el tipo no existe: se vuelve al listado.
 * - IllegalArgumentException -> los datos no son válidos: se vuelve al formulario.
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
        prepararFormulario(model, new TipoHabitacion(), "New room type", "/admin/tipos-habitacion/create");
        return "tipos-habitacion/formulario";
    }

    /** Crea el tipo de habitación que llenó el administrador. */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/create
    @PostMapping("/create")
    public String crear(@ModelAttribute TipoHabitacion tipo, Model model) {
        try {
            tipoHabitacionService.crear(tipo);
            return "redirect:/admin/tipos-habitacion/read";
        } catch (IllegalArgumentException datosInvalidos) {
            prepararFormulario(model, tipo, "New room type", "/admin/tipos-habitacion/create");
            model.addAttribute("error", datosInvalidos.getMessage());
            return "tipos-habitacion/formulario";
        }
    }

    /**
     * Muestra el formulario con los datos actuales del tipo para modificarlos.
     * URL: http://localhost:8080/admin/tipos-habitacion/update/{nombre}
     */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/update/{nombre}
    @GetMapping("/update/{nombre}")
    public String mostrarFormularioEdicion(@PathVariable("nombre") String nombre,
                                           Model model,
                                           RedirectAttributes redireccion) {
        try {
            TipoHabitacion tipo = tipoHabitacionService.buscarPorNombre(nombre);
            prepararFormulario(model, tipo, "Edit room type", "/admin/tipos-habitacion/update/" + nombre);
            return "tipos-habitacion/formulario";
        } catch (NoSuchElementException tipoInexistente) {
            redireccion.addFlashAttribute("error", tipoInexistente.getMessage());
            return "redirect:/admin/tipos-habitacion/read";
        }
    }

    /**
     * Guarda los cambios. El nombre de la URL es el que tenía el tipo antes de
     * editarlo, porque el administrador puede estar cambiando justamente el nombre.
     */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/update/{nombre}
    @PostMapping("/update/{nombre}")
    public String actualizar(@PathVariable("nombre") String nombreActual,
                             @ModelAttribute TipoHabitacion tipo,
                             Model model,
                             RedirectAttributes redireccion) {
        try {
            tipoHabitacionService.actualizar(nombreActual, tipo);
            return "redirect:/admin/tipos-habitacion/read";
        } catch (NoSuchElementException tipoInexistente) {
            redireccion.addFlashAttribute("error", tipoInexistente.getMessage());
            return "redirect:/admin/tipos-habitacion/read";
        } catch (IllegalArgumentException datosInvalidos) {
            prepararFormulario(model, tipo, "Edit room type", "/admin/tipos-habitacion/update/" + nombreActual);
            model.addAttribute("error", datosInvalidos.getMessage());
            return "tipos-habitacion/formulario";
        }
    }

    /**
     * Elimina un tipo de habitación del catálogo.
     * Se usa POST y no GET porque es una acción que modifica datos.
     */
    // Full URL: http://localhost:8080/admin/tipos-habitacion/delete/{nombre}
    @PostMapping("/delete/{nombre}")
    public String eliminar(@PathVariable("nombre") String nombre, RedirectAttributes redireccion) {
        try {
            tipoHabitacionService.eliminar(nombre);
        } catch (NoSuchElementException tipoInexistente) {
            redireccion.addFlashAttribute("error", tipoInexistente.getMessage());
        }

        return "redirect:/admin/tipos-habitacion/read";
    }

    /** Atributos que necesita la vista del formulario, tanto al crear como al editar. */
    private void prepararFormulario(Model model, TipoHabitacion tipo, String titulo, String accion) {
        model.addAttribute("tipo", tipo);
        model.addAttribute("titulo", titulo);
        model.addAttribute("accion", accion);
    }
}
