package com.example.demo.repository;

import com.example.demo.entities.Reservation;
import com.example.demo.entities.enums.ReservationStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Acceso a las reservas que requieren cambios automáticos de estado. */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    /* Busca reservas con el estado indicado cuya fecha de llegada sea anterior a la fecha dada. */
    List<Reservation> findByStatusAndCheckInDateBefore(ReservationStatus status, LocalDate date);

    /* Busca todas las reservas asociadas a un cliente ordenadas por fecha de creación descendente. */
    List<Reservation> findByClient_ClientIdOrderByCreatedAtDesc(Integer clientId);
}
