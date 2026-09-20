package com.example.demo.controller;

import com.example.demo.entities.Administrator;
import com.example.demo.errors.InvalidAdministratorDataException;
import com.example.demo.service.interfaces.AdministratorService;
import com.example.demo.service.interfaces.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/** Listado de consulta y edición desde My Profile con contraseña. */
@Controller
public class AdministratorController {
    @Autowired
    private AdministratorService administrators;
    @Autowired
    private LoginService login;

    @Autowired
    private com.example.demo.config.ApplicationClock applicationClock;

    @GetMapping("/admin/panel")
    public String panel(Model model, @RequestParam(required = false) Integer adminId) {
        if (adminId != null) return "redirect:/admin/panel/" + adminId;
        prepareClock(model);
        model.addAttribute("profileUrl", "/staff/login");
        model.addAttribute("panelUrl", "/admin/panel");
        model.addAttribute("panelTitle", "Admin panel");
        return "admin/panel";
    }

    @GetMapping("/admin/panel/{id}")
    public String ownPanel(@PathVariable Integer id, Model model) {
        prepareClock(model);
        administrators.findById(id);
        prepareNavigation(model, id);
        model.addAttribute("panelTitle", "Admin panel");
        return "admin/panel";
    }

    @GetMapping("/admin/administrators/read")
    public String listAdministrators(@RequestParam(required = false) Integer adminId, Model model) {
        if (adminId != null) prepareNavigation(model, adminId);
        model.addAttribute("administradores", administrators.listAdministrators());
        return "administrators/list";
    }

    @GetMapping("/admin/administrators/read/{id}")
    public String showDetails(@PathVariable Integer id, @RequestParam(required = false) Integer adminId, Model model) {
        if (adminId != null) prepareNavigation(model, adminId);
        model.addAttribute("administrador", administrators.findById(id));
        model.addAttribute("ownProfile", false);
        return "administrators/details";
    }

    @GetMapping("/admins/read/{id}")
    public String myProfile(@PathVariable Integer id, Model model) {
        model.addAttribute("administrador", administrators.findById(id));
        model.addAttribute("ownProfile", true);
        prepareNavigation(model, id);
        return "administrators/details";
    }

    @GetMapping("/admins/update/{id}")
    public String showFormEditing(@PathVariable Integer id, Model model) {
        prepareForm(model, administrators.findById(id), id);
        return "administrators/form";
    }

    @PostMapping("/admins/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute Administrator administrator,
                         @RequestParam(defaultValue = "") String passwordCurrent, Model model) {
        try {
            login.confirmAdministratorPassword(id, passwordCurrent);
            administrators.update(id, administrator);
            return "redirect:/admins/read/" + id;
        } catch (InvalidAdministratorDataException | SecurityException exception) {
            prepareForm(model, administrator, id);
            model.addAttribute("error", exception.getMessage());
            return "administrators/form";
        }
    }

    @GetMapping("/admins/{adminId}/administrators/create")
    public String createForm(@PathVariable Integer adminId, Model model) {
        administrators.findById(adminId);
        prepareCreateForm(model, new Administrator(), adminId);
        return "administrators/form";
    }

    @PostMapping("/admins/{adminId}/administrators/create")
    public String create(@PathVariable Integer adminId, @ModelAttribute Administrator administrator,
                         @RequestParam(defaultValue = "") String passwordCurrent, Model model) {
        try {
            login.confirmAdministratorPassword(adminId, passwordCurrent);
            administrators.create(administrator);
            return "redirect:/admin/administrators/read?adminId=" + adminId;
        } catch (InvalidAdministratorDataException | SecurityException exception) {
            administrator.setPassword(null);
            prepareCreateForm(model, administrator, adminId);
            model.addAttribute("error", exception.getMessage());
            return "administrators/form";
        }
    }

    @PostMapping("/admins/delete/{id}")
    public String delete(@PathVariable Integer id, @RequestParam(defaultValue = "") String passwordCurrent,
                         Model model) {
        try {
            login.confirmAdministratorPassword(id, passwordCurrent);
            administrators.delete(id);
            return "redirect:/";
        } catch (SecurityException | com.example.demo.errors.DeletionRestrictedException exception) {
            model.addAttribute("error", exception.getMessage());
            return myProfile(id, model);
        }
    }

    private void prepareCreateForm(Model model, Administrator administrator, Integer adminId) {
        prepareNavigation(model, adminId);
        model.addAttribute("administrador", administrator);
        model.addAttribute("esCreacion", true);
        model.addAttribute("titulo", "Create administrator");
        model.addAttribute("accion", "/admins/" + adminId + "/administrators/create");
        model.addAttribute("cancelUrl", "/admin/administrators/read?adminId=" + adminId);
    }

    private void prepareClock(Model model) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now(applicationClock);
        model.addAttribute("applicationDateTime", now);
        model.addAttribute("applicationDateTimeInput",
                now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        model.addAttribute("applicationClockFixed", applicationClock.isFixed());
    }

    private void prepareNavigation(Model model, Integer id) {
        model.addAttribute("adminId", id);
        model.addAttribute("profileUrl", "/admins/read/" + id);
        model.addAttribute("panelUrl", "/admin/panel/" + id);
    }

    private void prepareForm(Model model, Administrator administrator, Integer id) {
        model.addAttribute("administrador", administrator);
        model.addAttribute("titulo", "Edit my details");
        model.addAttribute("accion", "/admins/update/" + id);
        model.addAttribute("cancelUrl", "/admins/read/" + id);
        prepareNavigation(model, id);
    }
}
