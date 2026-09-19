package com.example.demo.controller;

import com.example.demo.entities.Administrator;
import com.example.demo.errors.InvalidAdministratorDataException;
import com.example.demo.service.AuthenticatedAccount;
import com.example.demo.service.interfaces.AdministratorService;
import com.example.demo.service.interfaces.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** Cuentas administrativas: creación desde el panel y edición del perfil propio. */
@Controller
public class AdministratorController {
    private final AdministratorService administrators;
    private final LoginService login;

    public AdministratorController(AdministratorService administrators, LoginService login) {
        this.administrators = administrators;
        this.login = login;
    }

    @InitBinder("administrator")
    public void allowedFields(WebDataBinder binder) {
        binder.setAllowedFields("name", "email", "password");
    }

    @GetMapping("/admin/administrators/read")
    public String listAdministrators(Model model) {
        model.addAttribute("administradores", administrators.listAdministrators());
        return "administrators/list";
    }

    @GetMapping({"/admin/administrators/read/{adminId}", "/admins/read/{adminId}"})
    public String showDetails(@PathVariable Integer adminId, Model model) {
        model.addAttribute("administrador", administrators.findById(adminId));
        return "administrators/details";
    }

    @GetMapping("/admin/administrators/create")
    public String showCreate(Model model) {
        prepareForm(model, new Administrator(), null);
        return "administrators/form";
    }

    @PostMapping("/admin/administrators/create")
    public String create(@ModelAttribute Administrator administrator, Model model) {
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
        requireOwnAccount(adminId, session);
        prepareForm(model, administrators.findById(adminId), adminId);
        return "administrators/form";
    }

    @PostMapping({"/admin/administrators/update/{adminId}", "/admins/update/{adminId}"})
    public String update(@PathVariable Integer adminId, @ModelAttribute Administrator administrator,
                         @RequestParam(defaultValue = "") String passwordCurrent, Model model, HttpSession session) {
        AuthenticatedAccount account = requireOwnAccount(adminId, session);
        try {
            login.confirmPassword(account, passwordCurrent);
            administrators.update(adminId, administrator);
            return "redirect:/admins/read/" + adminId;
        } catch (InvalidAdministratorDataException | SecurityException exception) {
            prepareForm(model, administrator, adminId);
            model.addAttribute("error", exception.getMessage());
            return "administrators/form";
        }
    }

    private AuthenticatedAccount requireOwnAccount(Integer adminId, HttpSession session) {
        AuthenticatedAccount account = (AuthenticatedAccount) session.getAttribute("account");
        if (account == null || account.role() != AuthenticatedAccount.Role.ADMINISTRATOR || !adminId.equals(account.id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own administrator profile.");
        }
        return account;
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
