package com.example.demo.scheduler;

import com.example.demo.service.interfaces.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Ejecuta diariamente la revisión de reservas pendientes vencidas. */
@Component
public class ReservationExpirationJob {

    @Autowired
    private ReservationService reservationService;

    /** Revisa las reservas pendientes todos los días a las 00:05 en Bogotá. */
    @Scheduled(cron = "0 5 0 * * *", zone = "America/Bogota")
    public void cancelExpiredPendingReservations() {
        reservationService.cancelExpiredPendingReservations();
    }
}
