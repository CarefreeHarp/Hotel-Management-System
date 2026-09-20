package com.example.demo.controller;

import com.example.demo.entities.Administrator;
import com.example.demo.errors.InvalidAdministratorDataException;
import com.example.demo.service.interfaces.AdministratorService;
import com.example.demo.service.interfaces.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/** Cuentas administrativas: creación desde el panel y edición del perfil propio. */
@Controller
public class AdministratorController {
    @Autowired
    private AdministratorService administrators;
    @Autowired
    private LoginService login;

    @GetMapping("/admin/administrators/read")
    public String listAdministrators(Model model, HttpSession session) {
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        model.addAttribute("administradores", administrators.listAdministrators());
        return "administrators/list";
    }

    @GetMapping({"/admin/administrators/read/{adminId}", "/admins/read/{adminId}"})
    public String showDetails(@PathVariable Integer adminId, Model model, HttpSession session) {
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        model.addAttribute("administrador", administrators.findById(adminId));
        return "administrators/details";
    }

    @GetMapping("/admin/administrators/create")
    public String showCreate(Model model, HttpSession session) {
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        prepareForm(model, new Administrator(), null);
        return "administrators/form";
    }

    @PostMapping("/admin/administrators/create")
    public String create(@ModelAttribute Administrator administrator, Model model, HttpSession session) {
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        try {
            administrators.create(administrator);
            return "redirect:/admin/administrators/read/" + administrator.getAdminId();
        } catch (InvalidAdministratorDataException exception) {
            prepareForm(model, administrator, null);
            model.addAttribute("error", exception.getMessage());
            return "administrators/form";
        }
    }

    @GetMapping({"/admin/administrators/update/{adminId}", "/admins/update/{adminId}"})
    public String showFormEditing(@PathVariable Integer adminId, Model model, HttpSession session) {
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        requireOwnAccount(adminId, session);
        prepareForm(model, administrators.findById(adminId), adminId);
        return "administrators/form";
    }

    @PostMapping({"/admin/administrators/update/{adminId}", "/admins/update/{adminId}"})
    public String update(@PathVariable Integer adminId, @ModelAttribute Administrator administrator,
                         @RequestParam(defaultValue = "") String passwordCurrent, Model model, HttpSession session) {
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        requireOwnAccount(adminId, session);
        try {
            login.confirmAdministratorPassword(adminId, passwordCurrent);
            administrators.update(adminId, administrator);
            return "redirect:/admins/read/" + adminId;
        } catch (InvalidAdministratorDataException | SecurityException exception) {
            prepareForm(model, administrator, adminId);
            model.addAttribute("error", exception.getMessage());
            return "administrators/form";
        }
    }

    private void requireOwnAccount(Integer adminId, HttpSession session) {
        if (!adminId.equals(session.getAttribute("adminId"))) {
            throw new SecurityException("You can only edit your own administrator profile.");
        }
    }

    private void prepareForm(Model model, Administrator administrator, Integer adminId) {
        model.addAttribute("administrador", administrator);
        model.addAttribute("adminId", adminId);
        model.addAttribute("esCreacion", adminId == null);
        model.addAttribute("titulo", adminId == null ? "Create administrator" : "Edit my details");
        model.addAttribute("accion", adminId == null ? "/admin/administrators/create" : "/admins/update/" + adminId);
        model.addAttribute("cancelUrl", adminId == null ? "/admin/administrators/read" : "/admins/read/" + adminId);
    }
}
