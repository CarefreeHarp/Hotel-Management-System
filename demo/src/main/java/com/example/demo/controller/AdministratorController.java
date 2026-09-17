package com.example.demo.controller;

import com.example.demo.entities.Administrator;
import com.example.demo.errors.InvalidAdministratorDataException;
import com.example.demo.service.interfaces.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CAPA DE CONTROLADOR: pantalla de administradores del portal.
 *
 * Por ahora el módulo solo tiene READ y UPDATE, así que hay tres rutas: el
 * listado, el detalle de un administrador y el formulario de edición (que se
 * muestra con GET y se guarda con POST sobre la misma URL).
 *
 * El controlador NO valida nada: llama al servicio. Los datos inválidos del
 * formulario vuelven al formulario con un aviso; que el administrador no exista
 * se atiende de forma centralizada en GlobalExceptionHandler.
 */
@Controller
@RequestMapping("/admin/administrators")
public class AdministratorController {

    @Autowired
    AdministratorService administratorService;

    /**
     * Listado de todos los administradores.
     * URL: http://localhost:8080/admin/administrators/read
     */
    // Full URL: http://localhost:8080/admin/administrators/read
    @GetMapping("/read")
    public String listAdministrators(Model model) {
        model.addAttribute("administradores", administratorService.listAdministrators());
        return "administrators/list";
    }

    /**
     * Detalle de un administrador.
     * URL: http://localhost:8080/admin/administrators/read/{adminId}
     */
    // Full URL: http://localhost:8080/admin/administrators/read/{adminId}
    @GetMapping("/read/{adminId}")
    public String showDetails(@PathVariable Integer adminId, Model model) {
        model.addAttribute("administrador", administratorService.findById(adminId));
        return "administrators/details";
    }

    /**
     * Muestra el formulario con los datos actuales del administrador.
     * URL: http://localhost:8080/admin/administrators/update/{adminId}
     */
    // Full URL: http://localhost:8080/admin/administrators/update/{adminId}
    @GetMapping("/update/{adminId}")
    public String showFormEditing(@PathVariable Integer adminId, Model model) {
        Administrator administrator = administratorService.findById(adminId);
        prepareForm(model, administrator, adminId);
        return "administrators/form";
    }

    /**
     * Guarda los cambios. El id de la URL identifica la cuenta incluso si el
     * administrador cambia su correo.
     */
    // Full URL: http://localhost:8080/admin/administrators/update/{adminId}
    @PostMapping("/update/{adminId}")
    public String update(@PathVariable Integer adminId,
                         @ModelAttribute Administrator administrator,
                         Model model) {
        try {
            administratorService.update(adminId, administrator);
            return "redirect:/admin/administrators/read/" + adminId;
        } catch (InvalidAdministratorDataException exception) {
            prepareForm(model, administrator, adminId);
            model.addAttribute("error", exception.getMessage());
            return "administrators/form";
        }
    }

    /**
     * Atributos que necesita la vista del formulario de edición.
     * El adminId se manda aparte porque el formulario no envía el id de vuelta:
     * la vista lo necesita para armar la URL de guardado y la de cancelar.
     */
    private void prepareForm(Model model, Administrator administrator, Integer adminId) {
        model.addAttribute("administrador", administrator);
        model.addAttribute("adminId", adminId);
        model.addAttribute("titulo", "Edit administrator");
        model.addAttribute("accion", "/admin/administrators/update/" + adminId);
    }
}
