package com.example.demo.errors;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SecurityException.class)
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.FORBIDDEN)
    public String handleAccessDenied(SecurityException exception, Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(DeletionRestrictedException.class)
    public String handleDeletionRestricted(DeletionRestrictedException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

}
