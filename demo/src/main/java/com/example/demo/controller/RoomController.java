package com.example.demo.controller;

import com.example.demo.entities.enums.RoomStatus;
import com.example.demo.entities.Room;
import com.example.demo.errors.InvalidRoomDataException;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.service.RoomService;
import com.example.demo.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CAPA DE CONTROLADOR: CRUD de habitaciones del portal de administrador.
 *
 * El controlador no valida datos: llama al servicio. Los datos inválidos vuelven
 * al formulario con un aviso; los demás errores de negocio se atienden de forma
 * centralizada en GlobalExceptionHandler.
 *
 * RELACIÓN CON EL TIPO DE HABITACIÓN: en la base de datos Room guarda una llave
 * foránea hacia RoomType, y en Java eso es un objeto RoomType completo. El
 * formulario, en cambio, solo puede mandar el id que se eligió en el desplegable,
 * así que ese id llega como parámetro aparte y aquí se cambia por el tipo real
 * que está guardado en la base de datos.
 */
@Controller
@RequestMapping("/admin/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    RoomTypeService roomTypeService;

    // Full URL: http://localhost:8080/admin/rooms/read
    @GetMapping("/read")
    public String listRooms(Model model) {
        model.addAttribute("habitaciones", roomService.listRooms());
        return "rooms/list";
    }

    // Full URL: http://localhost:8080/admin/rooms/create
    @GetMapping("/create")
    public String showFormCreacion(Model model) {
        // Se arma con el builder y no con new Room() porque así la lista de fotos
        // secundarias llega vacía en vez de en null, que es lo que espera la vista.
        prepareForm(model, Room.builder().build(), "Create room", "/admin/rooms/create");
        return "rooms/form";
    }

    // Full URL: http://localhost:8080/admin/rooms/create
    @PostMapping("/create")
    public String create(@ModelAttribute Room room,
                         @RequestParam(value = "roomTypeId", required = false) Integer roomTypeId,
                         Model model) {
        try {
            if (roomTypeId != null) {
                room.setRoomType(roomTypeService.findById(roomTypeId));
            }
            roomService.create(room);
            return "redirect:/admin/rooms/read";
        } catch (ResourceNotFoundException | InvalidRoomDataException exception) {
            prepareForm(model, room, "Create room", "/admin/rooms/create");
            model.addAttribute("error", exception.getMessage());
            return "rooms/form";
        }
    }

    // Full URL: http://localhost:8080/admin/rooms/read/{number}
    @GetMapping("/read/{number}")
    public String verDetalle(@PathVariable int number, Model model) {
        // La vista llega al tipo navegando la relación (habitacion.roomType),
        // así que no hace falta mandarlo como un atributo aparte.
        model.addAttribute("habitacion", roomService.findByNumber(number));
        return "rooms/details";
    }

    // Full URL: http://localhost:8080/admin/rooms/update/{number}
    @GetMapping("/update/{number}")
    public String showFormEditing(@PathVariable int number, Model model) {
        Room room = roomService.findByNumber(number);
        prepareForm(model, room, "Update room", "/admin/rooms/update/" + number);
        return "rooms/form";
    }

    // Full URL: http://localhost:8080/admin/rooms/update/{number}
    @PostMapping("/update/{number}")
    public String update(@PathVariable int number,
                             @ModelAttribute Room room,
                             @RequestParam(value = "roomTypeId", required = false) Integer roomTypeId,
                             Model model) {
        try {
            if (roomTypeId != null) {
                room.setRoomType(roomTypeService.findById(roomTypeId));
            }
            roomService.update(number, room);
            return "redirect:/admin/rooms/read";
        } catch (ResourceNotFoundException | InvalidRoomDataException exception) {
            prepareForm(model, room, "Update room", "/admin/rooms/update/" + number);
            model.addAttribute("error", exception.getMessage());
            return "rooms/form";
        }
    }

    // Full URL: http://localhost:8080/admin/rooms/delete/{number}
    @PostMapping("/delete/{number}")
    public String delete(@PathVariable int number) {
        roomService.delete(number);
        return "redirect:/admin/rooms/read";
    }

    private void prepareForm(Model model, Room room, String title, String action) {
        model.addAttribute("habitacion", room);
        model.addAttribute("tipos", roomTypeService.listTypes());
        model.addAttribute("estados", RoomStatus.values());
        model.addAttribute("titulo", title);
        model.addAttribute("accion", action);
    }
}
