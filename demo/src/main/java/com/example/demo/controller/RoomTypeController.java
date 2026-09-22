package com.example.demo.controller;

import com.example.demo.entities.RoomType;
import com.example.demo.errors.DeletionRestrictedException;
import com.example.demo.errors.InvalidRoomTypeDataException;
import com.example.demo.service.interfaces.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
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
 *
 * El controlador NO valida nada: llama al servicio. Los datos inválidos del
 * formulario vuelven al formulario con un aviso; que el tipo no exista se
 * atiende de forma centralizada en GlobalExceptionHandler.
 */
@Controller
@RequestMapping("/admin/room-types")
public class RoomTypeController {

    @Autowired
    RoomTypeService roomTypeService;

    /**
     * Listado del catálogo de tipos de habitación.
     * URL: http://localhost:8080/admin/room-types/read
     */
    // Full URL: http://localhost:8080/admin/room-types/read
    @GetMapping("/read")
    public String listTypes(Model model, @RequestParam(required = false) Integer adminId, @RequestParam(required = false) Integer operatorId) {
        prepareNavigation(model, adminId, operatorId);
        model.addAttribute("tipos", roomTypeService.listTypes());
        return "room-types/list";
    }

    // Full URL: http://localhost:8080/admin/room-types/read/{name}
    @GetMapping("/read/{name}")
    public String showDetails(@PathVariable String name, Model model, @RequestParam(required = false) Integer adminId, @RequestParam(required = false) Integer operatorId) {
        prepareNavigation(model, adminId, operatorId);
        model.addAttribute("tipo", roomTypeService.findByName(name));
        return "room-types/details";
    }

    /**
     * Muestra el formulario de creación vacío.
     * URL: http://localhost:8080/admin/room-types/create
     */
    // Full URL: http://localhost:8080/admin/room-types/create
    @GetMapping("/create")
    public String showFormCreacion(Model model, @RequestParam(required = false) Integer adminId, @RequestParam(required = false) Integer operatorId) {
        prepareNavigation(model, adminId, operatorId);
        prepareForm(model, RoomType.builder().build(), "New room type", "/admin/room-types/create");
        return "room-types/form";
    }

    /** Crea el tipo de habitación que llenó el administrador. */
    // Full URL: http://localhost:8080/admin/room-types/create
    @PostMapping("/create")
    public String create(@ModelAttribute RoomType type, Model model, @RequestParam(required = false) Integer adminId, @RequestParam(required = false) Integer operatorId) {
        prepareNavigation(model, adminId, operatorId);
        try {
            roomTypeService.create(type);
            return "redirect:/admin/room-types/read" + navigationQuery(adminId, operatorId);
        } catch (InvalidRoomTypeDataException exception) {
            prepareForm(model, type, "New room type", "/admin/room-types/create");
            model.addAttribute("error", exception.getMessage());
            return "room-types/form";
        }
    }

    /**
     * Muestra el formulario con los datos actuales del tipo para modificarlos.
     * URL: http://localhost:8080/admin/room-types/update/{name}
     */
    // Full URL: http://localhost:8080/admin/room-types/update/{name}
    @GetMapping("/update/{name}")
    public String showFormEditing(@PathVariable("name") String name, Model model, @RequestParam(required = false) Integer adminId, @RequestParam(required = false) Integer operatorId) {
        prepareNavigation(model, adminId, operatorId);
        RoomType type = roomTypeService.findByName(name);
        prepareForm(model, type, "Edit room type", "/admin/room-types/update/" + name);
        return "room-types/form";
    }

    /**
     * Guarda los cambios. El name de la URL es el que tenía el tipo antes de
     * editarlo, porque el administrador puede estar cambiando justamente el name.
     */
    // Full URL: http://localhost:8080/admin/room-types/update/{name}
    @PostMapping("/update/{name}")
    public String update(@PathVariable("name") String currentName,
                             @ModelAttribute RoomType type,
                             Model model, @RequestParam(required = false) Integer adminId, @RequestParam(required = false) Integer operatorId) {
        prepareNavigation(model, adminId, operatorId);
        try {
            roomTypeService.update(currentName, type);
            return "redirect:/admin/room-types/read" + navigationQuery(adminId, operatorId);
        } catch (InvalidRoomTypeDataException exception) {
            prepareForm(model, type, "Edit room type", "/admin/room-types/update/" + currentName);
            model.addAttribute("error", exception.getMessage());
            return "room-types/form";
        }
    }

    /**
     * Elimina un tipo de habitación del catálogo.
     * Se usa POST y no GET porque es una acción que modifica datos.
     */
    // Full URL: http://localhost:8080/admin/room-types/delete/{name}
    @PostMapping("/delete/{name}")
    public String delete(@PathVariable("name") String name, @RequestParam(required = false) Integer adminId, @RequestParam(required = false) Integer operatorId, Model model) {
        prepareNavigation(model, adminId, operatorId);
        try {
            roomTypeService.delete(name);
            return "redirect:/admin/room-types/read" + navigationQuery(adminId, operatorId);
        } catch (DeletionRestrictedException exception) {
            // La restriccion no es un fallo del sistema sino una regla de negocio:
            // se avisa dentro del listado, igual que los errores de los formularios.
            model.addAttribute("error", exception.getMessage());
            return listTypes(model, adminId, operatorId);
        }
    }

    /** Atributos que necesita la vista del formulario, tanto al crear como al editar. */
    private void prepareForm(Model model, RoomType type, String title, String action) {
        model.addAttribute("tipo", type);
        model.addAttribute("titulo", title);
        model.addAttribute("accion", action);
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
