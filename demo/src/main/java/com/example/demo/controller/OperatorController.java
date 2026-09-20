package com.example.demo.controller;

import com.example.demo.entities.Operator;
import com.example.demo.errors.InvalidOperatorDataException;
import com.example.demo.service.interfaces.AdministratorService;
import com.example.demo.service.interfaces.LoginService;
import com.example.demo.service.interfaces.OperatorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** El operario edita su perfil; el administrador gestiona sus operarios a cargo. */
@Controller
public class OperatorController {
    @Autowired
    private OperatorService operators;
    @Autowired
    private AdministratorService administrators;
    @Autowired
    private LoginService login;

    @GetMapping("/admin/operators/read")
    public String listOperators(Model model, HttpSession session) {
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        model.addAttribute("operarios", operators.listByAdministrator((Integer) session.getAttribute("adminId")));
        return "operators/list";
    }

    @GetMapping({"/admin/operators/read/{operatorId}", "/operators/read/{operatorId}"})
    public String showDetails(@PathVariable Integer operatorId, Model model, HttpSession session) {
        if (!login.isStaff((Integer) session.getAttribute("adminId"), (Integer) session.getAttribute("operatorId"))) {
            return "redirect:/login";
        }
        model.addAttribute("operario", accessibleOperator(operatorId, session));
        return "operators/details";
    }

    @GetMapping("/admin/operators/create")
    public String showCreate(Model model, HttpSession session) {
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        prepareForm(model, new Operator(), null, session);
        return "operators/form";
    }

    @PostMapping("/admin/operators/create")
    public String create(@ModelAttribute Operator operator, Model model, HttpSession session) {
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        try {
            // La asignación se toma de la sesión, nunca de un campo manipulable.
            operators.create(operator, (Integer) session.getAttribute("adminId"));
            return "redirect:/admin/operators/read/" + operator.getOperatorId();
        } catch (InvalidOperatorDataException exception) {
            prepareForm(model, operator, null, session);
            model.addAttribute("error", exception.getMessage());
            return "operators/form";
        }
    }

    @GetMapping({"/admin/operators/update/{operatorId}", "/operators/update/{operatorId}"})
    public String showFormEditing(@PathVariable Integer operatorId, Model model, HttpSession session) {
        if (!login.isStaff((Integer) session.getAttribute("adminId"), (Integer) session.getAttribute("operatorId"))) {
            return "redirect:/login";
        }
        prepareForm(model, accessibleOperator(operatorId, session), operatorId, session);
        return "operators/form";
    }

    @PostMapping({"/admin/operators/update/{operatorId}", "/operators/update/{operatorId}"})
    public String update(@PathVariable Integer operatorId, @ModelAttribute Operator operator,
                         @RequestParam(defaultValue = "") String passwordCurrent, Model model, HttpSession session) {
        if (!login.isStaff((Integer) session.getAttribute("adminId"), (Integer) session.getAttribute("operatorId"))) {
            return "redirect:/login";
        }
        Operator registered = accessibleOperator(operatorId, session);
        try {
            confirmPassword(session, passwordCurrent);
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
        if (!login.isAdministrator((Integer) session.getAttribute("adminId"))) {
            return "redirect:/login";
        }
        accessibleOperator(operatorId, session);
        try {
            confirmPassword(session, passwordCurrent);
            operators.deleteManagedBy(operatorId, (Integer) session.getAttribute("adminId"));
            redirect.addFlashAttribute("success", "Operator deleted.");
        } catch (SecurityException exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/admin/operators/read";
    }

    private Operator accessibleOperator(Integer operatorId, HttpSession session) {
        Integer adminId = (Integer) session.getAttribute("adminId");
        Integer loggedOperatorId = (Integer) session.getAttribute("operatorId");
        if (login.isAdministrator(adminId)) {
            return operators.findManagedBy(operatorId, adminId);
        }
        if (!login.isOperator(loggedOperatorId) || !operatorId.equals(loggedOperatorId)) {
            throw new SecurityException("You can only access your own operator profile.");
        }
        return operators.findById(operatorId);
    }

    private void confirmPassword(HttpSession session, String password) {
        Integer adminId = (Integer) session.getAttribute("adminId");
        if (login.isAdministrator(adminId)) {
            login.confirmAdministratorPassword(adminId, password);
        } else {
            login.confirmOperatorPassword((Integer) session.getAttribute("operatorId"), password);
        }
    }

    private String prefix(HttpSession session) {
        if (session.getAttribute("adminId") != null) return "/admin/operators";
        return "/operators";
    }

    private void prepareForm(Model model, Operator operator, Integer operatorId, HttpSession session) {
        Integer adminId = operatorId == null ? (Integer) session.getAttribute("adminId")
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
