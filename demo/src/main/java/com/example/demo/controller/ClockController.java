package com.example.demo.controller;

import com.example.demo.config.ApplicationClock;
import com.example.demo.service.interfaces.ReservationService;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Expone controles de prueba para la fuente de tiempo compartida. */
@Controller
public class ClockController {

    private final ApplicationClock applicationClock;
    private final ReservationService reservationService;

    public ClockController(ApplicationClock applicationClock, ReservationService reservationService) {
        this.applicationClock = applicationClock;
        this.reservationService = reservationService;
    }

    /** Configura una fecha y hora fija cuando se solicita desde la landing de pruebas. */
    @PostMapping("/clock/fixed")
    public String setFixedTime(
            @RequestParam("dateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        applicationClock.setFixedDateTime(dateTime);
        return "redirect:/";
    }

    /** Restaura la fuente de tiempo real cuando se solicita desde la landing de pruebas. */
    @PostMapping("/clock/system")
    public String useSystemTime() {
        applicationClock.useSystemTime();
        return "redirect:/";
    }

    /** Ejecuta manualmente las reglas que normalmente se disparan a medianoche. */
    @PostMapping("/clock/run-midnight-actions")
    public String runMidnightActions() {
        reservationService.runMidnightReservationAndRoomStatusActions();
        return "redirect:/";
    }
}
