package com.example.demo.controller;

import com.example.demo.entities.enums.RoomStatus;
import com.example.demo.entities.Room;
import com.example.demo.service.RoomService;
import com.example.demo.service.RoomTypeService;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CAPA DE CONTROLADOR: CRUD de habitaciones del portal de administrador.
 *
 * El controlador no valida datos: llama al servicio y decide la pantalla según
 * la excepción que este lance.
 *
 * - NoSuchElementException   -> la habitación (o su tipo) no existe: se vuelve al listado.
 * - IllegalArgumentException -> el formulario trae datos inválidos: se vuelve al formulario.
 */
@Controller
@RequestMapping("/admin/habitaciones")
public class RoomController {

    @Autowired(required = false)
    RoomService roomService;

    @Autowired(required = false)
    RoomTypeService roomTypeService;

    // Full URL: http://localhost:8080/admin/habitaciones/read
    @GetMapping("/read")
    public String listRooms(Model model) {
        model.addAttribute("habitaciones", roomService.listRooms());
        return "habitaciones/lista";
    }

    // Full URL: http://localhost:8080/admin/habitaciones/create
    @GetMapping("/create")
    public String showFormCreacion(Model model) {
        prepareForm(model, new Room(), "Create room", "/admin/habitaciones/create");
        return "habitaciones/formulario";
    }

    // Full URL: http://localhost:8080/admin/habitaciones/create
    @PostMapping("/create")
    public String create(@ModelAttribute Room room, Model model) {
        try {
            roomService.create(room);
            return "redirect:/admin/habitaciones/read";
        } catch (IllegalArgumentException dataInvalidos) {
            prepareForm(model, room, "Create room", "/admin/habitaciones/create");
            model.addAttribute("error", dataInvalidos.getMessage());
            return "habitaciones/formulario";
        }
    }

    // Full URL: http://localhost:8080/admin/habitaciones/read/{number}
    @GetMapping("/read/{number}")
    public String verDetalle(@PathVariable int number, Model model, RedirectAttributes redirectAttributes) {
        try {
            Room room = roomService.findByNumber(number);
            model.addAttribute("habitacion", room);
            model.addAttribute("type", room.getRoomType());
            return "habitaciones/detalle";
        } catch (NoSuchElementException roomNotFound) {
            redirectAttributes.addFlashAttribute("error", roomNotFound.getMessage());
            return "redirect:/admin/habitaciones/read";
        }
    }

    // Full URL: http://localhost:8080/admin/habitaciones/update/{number}
    @GetMapping("/update/{number}")
    public String showFormEditing(@PathVariable int number, Model model, RedirectAttributes redirectAttributes) {
        try {
            Room room = roomService.findByNumber(number);
            prepareForm(model, room, "Update room", "/admin/habitaciones/update/" + number);
            return "habitaciones/formulario";
        } catch (NoSuchElementException roomNotFound) {
            redirectAttributes.addFlashAttribute("error", roomNotFound.getMessage());
            return "redirect:/admin/habitaciones/read";
        }
    }

    // Full URL: http://localhost:8080/admin/habitaciones/update/{number}
    @PostMapping("/update/{number}")
    public String update(@PathVariable int number,
                             @ModelAttribute Room room,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            roomService.update(number, room);
            return "redirect:/admin/habitaciones/read";
        } catch (NoSuchElementException roomNotFound) {
            redirectAttributes.addFlashAttribute("error", roomNotFound.getMessage());
            return "redirect:/admin/habitaciones/read";
        } catch (IllegalArgumentException dataInvalidos) {
            prepareForm(model, room, "Update room", "/admin/habitaciones/update/" + number);
            model.addAttribute("error", dataInvalidos.getMessage());
            return "habitaciones/formulario";
        }
    }

    // Full URL: http://localhost:8080/admin/habitaciones/delete/{number}
    @PostMapping("/delete/{number}")
    public String delete(@PathVariable int number, RedirectAttributes redirectAttributes) {
        try {
            roomService.delete(number);
        } catch (NoSuchElementException roomNotFound) {
            redirectAttributes.addFlashAttribute("error", roomNotFound.getMessage());
        }

        return "redirect:/admin/habitaciones/read";
    }

    private void prepareForm(Model model, Room room, String title, String action) {
        model.addAttribute("habitacion", room);
        model.addAttribute("tipos", roomTypeService.listTypes());
        model.addAttribute("estados", RoomStatus.values());
        model.addAttribute("titulo", title);
        model.addAttribute("accion", action);
    }
}
