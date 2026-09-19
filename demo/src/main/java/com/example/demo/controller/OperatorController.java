package com.example.demo.controller;

import com.example.demo.entities.Operator;
import com.example.demo.errors.InvalidOperatorDataException;
import com.example.demo.service.AuthenticatedAccount;
import com.example.demo.service.interfaces.AdministratorService;
import com.example.demo.service.interfaces.LoginService;
import com.example.demo.service.interfaces.OperatorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** El operario edita su perfil; el administrador gestiona sus operarios a cargo. */
@Controller
public class OperatorController {
    private final OperatorService operators;
    private final AdministratorService administrators;
    private final LoginService login;

    public OperatorController(OperatorService operators, AdministratorService administrators, LoginService login) {
        this.operators = operators;
        this.administrators = administrators;
        this.login = login;
    }

    @InitBinder("operator")
    public void allowedFields(WebDataBinder binder) {
        binder.setAllowedFields("name", "lastName", "email", "password");
    }

    @GetMapping("/admin/operators/read")
    public String listOperators(Model model, HttpSession session) {
        model.addAttribute("operarios", operators.listByAdministrator(account(session).id()));
        return "operators/list";
    }

    @GetMapping({"/admin/operators/read/{operatorId}", "/operators/read/{operatorId}"})
    public String showDetails(@PathVariable Integer operatorId, Model model, HttpSession session) {
        model.addAttribute("operario", accessibleOperator(operatorId, session));
        return "operators/details";
    }

    @GetMapping("/admin/operators/create")
    public String showCreate(Model model, HttpSession session) {
        prepareForm(model, new Operator(), null, session);
        return "operators/form";
    }

    @PostMapping("/admin/operators/create")
    public String create(@ModelAttribute Operator operator, Model model, HttpSession session) {
        try {
            // La asignación se toma de la sesión, nunca de un campo manipulable.
            operators.create(operator, account(session).id());
            return "redirect:/admin/operators/read/" + operator.getOperatorId();
        } catch (InvalidOperatorDataException exception) {
            prepareForm(model, operator, null, session);
            model.addAttribute("error", exception.getMessage());
            return "operators/form";
        }
    }

    @GetMapping({"/admin/operators/update/{operatorId}", "/operators/update/{operatorId}"})
    public String showFormEditing(@PathVariable Integer operatorId, Model model, HttpSession session) {
        prepareForm(model, accessibleOperator(operatorId, session), operatorId, session);
        return "operators/form";
    }

    @PostMapping({"/admin/operators/update/{operatorId}", "/operators/update/{operatorId}"})
    public String update(@PathVariable Integer operatorId, @ModelAttribute Operator operator,
                         @RequestParam(defaultValue = "") String passwordCurrent, Model model, HttpSession session) {
        Operator registered = accessibleOperator(operatorId, session);
        try {
            login.confirmPassword(account(session), passwordCurrent);
            operators.update(operatorId, operator, registered.getAdmin().getAdminId());
            return "redirect:" + prefix(session) + "/read/" + operatorId;
        } catch (InvalidOperatorDataException | SecurityException exception) {
            prepareForm(model, operator, operatorId, session);
            model.addAttribute("error", exception.getMessage());
            return "operators/form";
        }
    }

    @PostMapping("/admin/operators/delete/{operatorId}")
    public String delete(@PathVariable Integer operatorId, @RequestParam(defaultValue = "") String passwordCurrent,
                         HttpSession session, RedirectAttributes redirect) {
        accessibleOperator(operatorId, session);
        try {
            login.confirmPassword(account(session), passwordCurrent);
            operators.deleteManagedBy(operatorId, account(session).id());
            redirect.addFlashAttribute("success", "Operator deleted.");
        } catch (SecurityException exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/admin/operators/read";
    }

    private AuthenticatedAccount account(HttpSession session) {
        return (AuthenticatedAccount) session.getAttribute("account");
    }

    private Operator accessibleOperator(Integer operatorId, HttpSession session) {
        AuthenticatedAccount actor = account(session);
        if (actor.role() == AuthenticatedAccount.Role.ADMINISTRATOR) {
            return operators.findManagedBy(operatorId, actor.id());
        }
        if (actor.role() != AuthenticatedAccount.Role.OPERATOR || !operatorId.equals(actor.id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your own operator profile.");
        }
        return operators.findById(operatorId);
    }

    private String prefix(HttpSession session) {
        return account(session).role() == AuthenticatedAccount.Role.ADMINISTRATOR ? "/admin/operators" : "/operators";
    }

    private void prepareForm(Model model, Operator operator, Integer operatorId, HttpSession session) {
        Integer adminId = operatorId == null ? account(session).id()
                : accessibleOperator(operatorId, session).getAdmin().getAdminId();
        model.addAttribute("operario", operator);
        model.addAttribute("operatorId", operatorId);
        model.addAttribute("adminName", administrators.findById(adminId).getName());
        model.addAttribute("esCreacion", operatorId == null);
        model.addAttribute("titulo", operatorId == null ? "Create operator" : "Edit operator");
        model.addAttribute("accion", operatorId == null ? "/admin/operators/create" : prefix(session) + "/update/" + operatorId);
        model.addAttribute("cancelUrl", operatorId == null ? "/admin/operators/read" : prefix(session) + "/read/" + operatorId);
    }
}
