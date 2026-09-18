package com.example.demo.repository;

import com.example.demo.entities.Folio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Acceso a los folios almacenados en la base de datos. */
@Repository
public interface FolioRepository extends JpaRepository<Folio, Integer> {

    /* Busca el único folio emitido para la reserva indicada. */
    Optional<Folio> findByReservationReservationId(Integer reservationId);
}
