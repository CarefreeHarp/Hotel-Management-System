package com.example.demo.errors;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public String handleClientNotFound(ClientNotFoundException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public String handleRoomNotFound(RoomNotFoundException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(RoomTypeNotFoundException.class)
    public String handleRoomTypeNotFound(RoomTypeNotFoundException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public String handleServiceNotFound(ServiceNotFoundException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

}
