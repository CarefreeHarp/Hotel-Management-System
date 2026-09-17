package com.example.demo.service;

import com.example.demo.entities.Reservation;
import com.example.demo.entities.enums.ReservationStatus;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.service.interfaces.ReservationService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Implementa el vencimiento automático de reservas pendientes. */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    /** Cancela las reservas pendientes cuyo check-in ya ocurrió. */
    @Override
    @Transactional
    public int cancelExpiredPendingReservations() {
        List<Reservation> expiredReservations = reservationRepository
                .findByStatusAndCheckInDateBefore(ReservationStatus.PENDING, LocalDate.now());
        expiredReservations.forEach(reservation -> reservation.setStatus(ReservationStatus.CANCELLED));
        reservationRepository.saveAll(expiredReservations);
        return expiredReservations.size();
    }
}
