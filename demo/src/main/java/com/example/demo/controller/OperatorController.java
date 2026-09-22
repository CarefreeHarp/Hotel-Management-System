package com.example.demo.controller;

import com.example.demo.entities.Operator;
import com.example.demo.errors.DeletionRestrictedException;
import com.example.demo.errors.InvalidOperatorDataException;
import com.example.demo.service.interfaces.LoginService;
import com.example.demo.service.interfaces.OperatorService;
import com.example.demo.security.SessionAccess;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/** Listado de consulta y perfil del operario identificado en la URL. */
@Controller
public class OperatorController {
    @Autowired
    private OperatorService operators;
    @Autowired
    private LoginService login;
    @Autowired
    private com.example.demo.service.interfaces.AdministratorService administrators;

    @GetMapping("/operators/panel")
    public String panel(Model model, @RequestParam(required = false) Integer operatorId) {
        if (operatorId != null) return "redirect:/operators/panel/" + operatorId;
        model.addAttribute("profileUrl", "/staff/login");
        model.addAttribute("panelUrl", "/operators/panel");
        model.addAttribute("panelTitle", "Operator panel");
        return "admin/panel";
    }

    @GetMapping("/operators/panel/{id}")
    public String ownPanel(@PathVariable Integer id, Model model) {
        operators.findById(id);
        prepareNavigation(model, id);
        model.addAttribute("panelTitle", "Operator panel");
        return "admin/panel";
    }

    @GetMapping("/admin/operators/read")
    public String listOperators(Model model, @RequestParam(required = false) Integer adminId, HttpSession session) {
        // ADMIN y OPERATOR ven operarios; el cliente no.
        if (!SessionAccess.isStaff(session)) return SessionAccess.deniedStaff(session);
        if (adminId != null) return "redirect:/admins/" + adminId + "/operators";
        model.addAttribute("operarios", operators.listOperators());
        return "operators/list";
    }

    @GetMapping("/admin/operators/read/{id}")
    public String showDetails(@PathVariable Integer id, Model model, @RequestParam(required = false) Integer adminId, HttpSession session) {
        if (!SessionAccess.isStaff(session)) return SessionAccess.deniedStaff(session);
        if (adminId != null) return "redirect:/admins/" + adminId + "/operators/" + id;
        model.addAttribute("operario", operators.findById(id));
        model.addAttribute("ownProfile", false);
        return "operators/details";
    }

    @GetMapping("/operators/read/{id}")
    public String myProfile(@PathVariable Integer id, Model model, HttpSession session) {
        // Esta vista pinta ownProfile=true (botones de editar/borrar), asi que
        // solo la abre el propio operario o un administrador.
        boolean self = SessionAccess.isOperator(session)
                && id.equals(session.getAttribute("operatorId"));
        if (!self && !SessionAccess.isAdmin(session)) return SessionAccess.deniedStaff(session);
        model.addAttribute("operario", operators.findById(id));
        model.addAttribute("ownProfile", true);
        prepareNavigation(model, id);
        return "operators/details";
    }

    @GetMapping("/operators/update/{id}")
    public String showFormEditing(@PathVariable Integer id, Model model) {
        prepareForm(model, operators.findById(id), id);
        return "operators/form";
    }

    @PostMapping("/operators/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Operator operator,
                         @RequestParam(defaultValue = "") String passwordCurrent, Model model) {
        Operator registered = operators.findById(id);
        try {
            login.confirmOperatorPassword(id, passwordCurrent);
            operators.update(id, operator, registered.getAdmin().getAdminId());
            return "redirect:/operators/read/" + id;
        } catch (InvalidOperatorDataException | SecurityException exception) {
            prepareForm(model, operator, id);
            model.addAttribute("error", exception.getMessage());
            return "operators/form";
        }
    }

    @GetMapping("/admins/{adminId}/operators")
    public String managedList(@PathVariable Integer adminId, Model model, HttpSession session) {
        // Arbol de gestion del administrador: las vistas exponen crear/editar/borrar.
        if (!SessionAccess.isAdmin(session)) return SessionAccess.deniedStaff(session);
        prepareAdminNavigation(model, adminId);
        model.addAttribute("operarios", operators.listByAdministrator(adminId));
        return "operators/list";
    }

    @GetMapping("/admins/{adminId}/operators/{id}")
    public String managedDetails(@PathVariable Integer adminId, @PathVariable Integer id,
                                 Model model, HttpSession session) {
        if (!SessionAccess.isAdmin(session)) return SessionAccess.deniedStaff(session);
        prepareAdminNavigation(model, adminId);
        model.addAttribute("operario", operators.findManagedBy(id, adminId));
        model.addAttribute("ownProfile", false);
        model.addAttribute("managedProfile", true);
        return "operators/details";
    }

    @GetMapping("/admins/{adminId}/operators/create")
    public String managedCreateForm(@PathVariable Integer adminId, Model model) {
        prepareManagedForm(model, new Operator(), adminId, null);
        return "operators/form";
    }

    @PostMapping("/admins/{adminId}/operators/create")
    public String managedCreate(@PathVariable Integer adminId, @ModelAttribute Operator operator,
                                @RequestParam(defaultValue = "") String passwordCurrent, Model model) {
        try {
            login.confirmAdministratorPassword(adminId, passwordCurrent);
            operators.create(operator, adminId);
            return "redirect:/admins/" + adminId + "/operators";
        } catch (InvalidOperatorDataException | SecurityException exception) {
            operator.setPassword(null);
            prepareManagedForm(model, operator, adminId, null);
            model.addAttribute("error", exception.getMessage());
            return "operators/form";
        }
    }

    @GetMapping("/admins/{adminId}/operators/{id}/update")
    public String managedEditForm(@PathVariable Integer adminId, @PathVariable Integer id,
                                  Model model) {
        prepareManagedForm(model, operators.findManagedBy(id, adminId), adminId, id);
        return "operators/form";
    }

    @PostMapping("/admins/{adminId}/operators/{id}/update")
    public String managedUpdate(@PathVariable Integer adminId, @PathVariable Integer id,
                                @ModelAttribute Operator operator, @RequestParam(defaultValue = "") String passwordCurrent,
                                Model model) {
        operators.findManagedBy(id, adminId);
        try {
            login.confirmAdministratorPassword(adminId, passwordCurrent);
            operators.updateManagedBy(id, operator, adminId);
            return "redirect:/admins/" + adminId + "/operators/" + id;
        } catch (InvalidOperatorDataException | SecurityException exception) {
            prepareManagedForm(model, operator, adminId, id);
            model.addAttribute("error", exception.getMessage());
            return "operators/form";
        }
    }

    @PostMapping("/admins/{adminId}/operators/{id}/delete")
    public String managedDelete(@PathVariable Integer adminId, @PathVariable Integer id,
                                @RequestParam(defaultValue = "") String passwordCurrent,
                                Model model, HttpSession session) {
        operators.findManagedBy(id, adminId);
        try {
            login.confirmAdministratorPassword(adminId, passwordCurrent);
            operators.deleteManagedBy(id, adminId);
            return "redirect:/admins/" + adminId + "/operators";
        } catch (SecurityException | DeletionRestrictedException exception) {
            model.addAttribute("error", exception.getMessage());
            return managedDetails(adminId, id, model, session);
        }
    }

    private void prepareAdminNavigation(Model model, Integer adminId) {
        model.addAttribute("adminId", adminId);
        model.addAttribute("profileUrl", "/admins/read/" + adminId);
        model.addAttribute("panelUrl", "/admin/panel/" + adminId);
    }

    private void prepareManagedForm(Model model, Operator operator, Integer adminId, Integer id) {
        prepareAdminNavigation(model, adminId);
        model.addAttribute("operario", operator);
        model.addAttribute("adminName", administrators.findById(adminId).getName());
        model.addAttribute("esCreacion", id == null);
        model.addAttribute("titulo", id == null ? "Create operator" : "Edit operator");
        model.addAttribute("accion", "/admins/" + adminId + "/operators/" + (id == null ? "create" : id + "/update"));
        model.addAttribute("cancelUrl", "/admins/" + adminId + "/operators");
    }

    private void prepareNavigation(Model model, Integer id) {
        model.addAttribute("operatorId", id);
        model.addAttribute("profileUrl", "/operators/read/" + id);
        model.addAttribute("panelUrl", "/operators/panel/" + id);
    }

    private void prepareForm(Model model, Operator operator, Integer id) {
        model.addAttribute("operario", operator);
        model.addAttribute("adminName", operators.findById(id).getAdmin().getName());
        model.addAttribute("titulo", "Edit my details");
        model.addAttribute("accion", "/operators/update/" + id);
        model.addAttribute("cancelUrl", "/operators/read/" + id);
        prepareNavigation(model, id);
    }
}
