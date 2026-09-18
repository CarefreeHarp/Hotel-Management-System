package com.example.demo.service.interfaces;

import com.example.demo.entities.Reservation;
import java.time.LocalDate;
import java.util.List;

/** Reglas de negocio para mantener vigentes las reservas pendientes. */
public interface ReservationService {

    /** Crea una reserva pendiente y su folio inicial para el cliente autenticado. */
    Reservation createPendingReservation(
            Integer clientId, Integer roomId, LocalDate checkInDate, LocalDate checkOutDate, Integer guests,
            List<Integer> serviceIds);

    /** Crea una reserva y registra un pago aprobado tras validar los datos de tarjeta recibidos. */
    Reservation createReservationWithCardPayment(
            Integer clientId,
            Integer roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Integer guests,
            String paymentAmount,
            String cardholderName,
            String cardNumber,
            String expiryDate,
            String securityCode,
            List<Integer> serviceIds);

    int runMidnightReservationAndRoomStatusActions();
}
