package com.example.demo.controller;

import com.example.demo.service.interfaces.RoomTypeService;
import com.example.demo.security.SessionAccess;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final RoomTypeService roomTypeService;

    public IndexController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    // Full URL: http://localhost:8080/, http://localhost:8080/index, http://localhost:8080/home
    @GetMapping({"/", "/index", "/home"})
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("roomTypes", roomTypeService.listTypes());
        // getSession(false) a proposito: la landing es publica y no debe crear
        // sesion ni cookie a quien solo pasa a mirar.
        HttpSession session = request.getSession(false);
        String staffPanelUrl = null;
        String staffPanelLabel = null;
        if (session != null && SessionAccess.isAdmin(session)) {
            staffPanelUrl = "/admin/panel/" + session.getAttribute("adminId");
            staffPanelLabel = "Admin panel";
        } else if (session != null && SessionAccess.isOperator(session)) {
            staffPanelUrl = "/operators/panel/" + session.getAttribute("operatorId");
            staffPanelLabel = "Operator panel";
        }
        // Null para anonimos y clientes: la plantilla deja el boton de reserva.
        model.addAttribute("staffPanelUrl", staffPanelUrl);
        model.addAttribute("staffPanelLabel", staffPanelLabel);
        return "landing-page";
    }

    // Full URL: http://localhost:8080/suites
    @GetMapping("/suites")
    public String suites() {
        return "redirect:/#rooms-section";
    }

    // Full URL: http://localhost:8080/book-now
    @GetMapping("/book-now")
    public String bookNow() {
        return "redirect:/#book-section";
    }

    // Full URL: http://localhost:8080/experiences
    @GetMapping("/experiences")
    public String experiences() {
        return "redirect:/#experiences-section";
    }

    // Full URL: http://localhost:8080/getting-here
    @GetMapping("/getting-here")
    public String gettingHere() {
        return "redirect:/#book-section";
    }

    // Full URL: http://localhost:8080/explore
    @GetMapping("/explore")
    public String explore() {
        return "redirect:/#video-section";
    }

    // Full URL: http://localhost:8080/restaurant
    @GetMapping("/restaurant")
    public String restaurant() {
        return "redirect:/#services-title";
    }

    // Full URL: http://localhost:8080/awards
    @GetMapping("/awards")
    public String awards() {
        return "redirect:/#hero";
    }
}
