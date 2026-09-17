package com.example.demo.controller;

import com.example.demo.entities.Operator;
import com.example.demo.errors.InvalidOperatorDataException;
import com.example.demo.service.interfaces.AdministratorService;
import com.example.demo.service.interfaces.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * CAPA DE CONTROLADOR: pantalla de operarios del portal.
 *
 * Igual que el módulo de administradores, por ahora solo tiene READ y UPDATE.
 *
 * El administrador a cargo llega como un @RequestParam aparte y no dentro del
 * @ModelAttribute, porque el desplegable del formulario manda un número
 * (el adminId) y el atributo admin del operario es un objeto Administrator:
 * es el servicio el que busca ese administrador y lo asocia.
 */
@Controller
@RequestMapping("/admin/operators")
public class OperatorController {

    @Autowired
    OperatorService operatorService;

    @Autowired
    AdministratorService administratorService;

    /**
     * Listado de todos los operarios.
     * URL: http://localhost:8080/admin/operators/read
     */
    // Full URL: http://localhost:8080/admin/operators/read
    @GetMapping("/read")
    public String listOperators(Model model) {
        model.addAttribute("operarios", operatorService.listOperators());
        return "operators/list";
    }

    /**
     * Detalle de un operario.
     * URL: http://localhost:8080/admin/operators/read/{operatorId}
     */
    // Full URL: http://localhost:8080/admin/operators/read/{operatorId}
    @GetMapping("/read/{operatorId}")
    public String showDetails(@PathVariable Integer operatorId, Model model) {
        model.addAttribute("operario", operatorService.findById(operatorId));
        return "operators/details";
    }

    /**
     * Muestra el formulario con los datos actuales del operario.
     * URL: http://localhost:8080/admin/operators/update/{operatorId}
     */
    // Full URL: http://localhost:8080/admin/operators/update/{operatorId}
    @GetMapping("/update/{operatorId}")
    public String showFormEditing(@PathVariable Integer operatorId, Model model) {
        Operator operator = operatorService.findById(operatorId);
        prepareForm(model, operator, operatorId, operator.getAdmin().getAdminId());
        return "operators/form";
    }

    /**
     * Guarda los cambios. El id de la URL identifica la cuenta incluso si el
     * operario cambia su correo.
     */
    // Full URL: http://localhost:8080/admin/operators/update/{operatorId}
    @PostMapping("/update/{operatorId}")
    public String update(@PathVariable Integer operatorId,
                         @ModelAttribute Operator operator,
                         @RequestParam(required = false) Integer adminId,
                         Model model) {
        try {
            operatorService.update(operatorId, operator, adminId);
            return "redirect:/admin/operators/read/" + operatorId;
        } catch (InvalidOperatorDataException exception) {
            prepareForm(model, operator, operatorId, adminId);
            model.addAttribute("error", exception.getMessage());
            return "operators/form";
        }
    }

    /**
     * Atributos que necesita la vista del formulario de edición.
     * El operatorId y el adminId se mandan aparte porque el formulario no envía
     * de vuelta el id del operario ni el objeto Administrator completo: la vista
     * los necesita para armar las URLs y para marcar la opción seleccionada del
     * desplegable. La lista de administradores llena ese desplegable.
     */
    private void prepareForm(Model model, Operator operator, Integer operatorId, Integer adminId) {
        model.addAttribute("operario", operator);
        model.addAttribute("operatorId", operatorId);
        model.addAttribute("adminId", adminId);
        model.addAttribute("administradores", administratorService.listAdministrators());
        model.addAttribute("titulo", "Edit operator");
        model.addAttribute("accion", "/admin/operators/update/" + operatorId);
    }
}
