package com.example.demo.service.interfaces;

import com.example.demo.entities.Folio;
import com.example.demo.entities.FolioItem;
import com.example.demo.errors.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Define las operaciones de consulta para los folios y sus cargos.
 *
 * Los importes no se guardan en la base de datos: se calculan aquí, en la capa
 * de servicio, cada vez que se consultan.
 */
public interface FolioService {

    /** Devuelve el encabezado y los totales del folio indicado. */
    Folio findById(Integer folioId);

    /** Devuelve el folio asociado a una reserva ya oficializada. */
    Folio findByReservationId(Integer reservationId);

    /** Devuelve los ítems de detalle asociados al folio indicado. */
    List<FolioItem> listItemsByFolioId(Integer folioId);

    /** Base imponible del folio: alojamiento mas servicios. */
    BigDecimal calculateSubtotal(Integer folioId);

    /** Impuestos aplicados sobre la base imponible del folio. */
    BigDecimal calculateTaxes(Integer folioId);

    /** Importe final del folio, que deben cubrir los pagos. */
    BigDecimal calculateTotal(Integer folioId);

    /** Importe de cada cargo del folio, indexado por el identificador del cargo. */
    Map<Integer, BigDecimal> calculateItemSubtotals(Integer folioId);

    /**
     * Impuestos sobre una base imponible todavía sin folio.
     *
     * Lo usan las pantallas del flujo de reserva, que muestran el importe antes
     * de que exista el folio y deben coincidir con lo que luego se cobrará.
     */
    BigDecimal calculateTaxesFor(BigDecimal subtotal);

    /** Importe final sobre una base imponible todavía sin folio. */
    BigDecimal calculateTotalFor(BigDecimal subtotal);
}
