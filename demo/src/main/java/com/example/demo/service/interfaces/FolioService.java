package com.example.demo.service.interfaces;

import com.example.demo.entities.Folio;
import com.example.demo.entities.FolioItem;
import com.example.demo.errors.ResourceNotFoundException;
import java.util.List;

/** Define las operaciones de consulta para los folios y sus cargos. */
public interface FolioService {

    /** Devuelve el encabezado y los totales del folio indicado. */
    Folio findById(Integer folioId);

    /** Devuelve el folio asociado a una reserva ya oficializada. */
    Folio findByReservationId(Integer reservationId);

    /** Devuelve los ítems de detalle asociados al folio indicado. */
    List<FolioItem> listItemsByFolioId(Integer folioId);
}
