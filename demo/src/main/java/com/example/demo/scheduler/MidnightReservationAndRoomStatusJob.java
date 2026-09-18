package com.example.demo.scheduler;

import com.example.demo.service.interfaces.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Ejecuta diariamente las acciones de reservas y estados de habitaciones de medianoche. */
@Component
public class MidnightReservationAndRoomStatusJob {

    @Autowired
    private ReservationService reservationService;

    /** Cancela reservas vencidas y sincroniza la ocupación todos los días a las 00:00 en Bogotá. */
    @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
    public void runMidnightReservationAndRoomStatusActions() {
        reservationService.runMidnightReservationAndRoomStatusActions();
    }
}
