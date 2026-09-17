package com.example.demo.service.interfaces;

/** Reglas de negocio para mantener vigentes las reservas pendientes. */
public interface ReservationService {

    int cancelExpiredPendingReservations();
}
