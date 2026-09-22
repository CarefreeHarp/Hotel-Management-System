package com.example.demo.service;

import com.example.demo.entities.Folio;
import com.example.demo.entities.FolioItem;
import com.example.demo.entities.Reservation;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.repository.FolioItemRepository;
import com.example.demo.repository.FolioRepository;
import com.example.demo.service.interfaces.FolioService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa las consultas de lectura para los folios y sus ítems.
 *
 * El subtotal, los impuestos y el total dejaron de ser columnas: se calculan
 * aquí en cada consulta a partir de la tarifa pactada en la reserva y de los
 * cargos registrados, de modo que nunca puedan desincronizarse del detalle que
 * resumen.
 */
@Service
public class FolioServiceImpl implements FolioService {

    /** IVA aplicado sobre el alojamiento y los servicios. */
    private static final BigDecimal TAX_RATE = new BigDecimal("0.19");

    @Autowired
    private FolioRepository folioRepository;

    @Autowired
    private FolioItemRepository folioItemRepository;

    /** Busca el folio solicitado y reporta un error si no existe. */
    @Override
    public Folio findById(Integer folioId) {
        return folioRepository.findById(folioId)
                .orElseThrow(() -> new ResourceNotFoundException("The folio does not exist."));
    }

    /** Busca el folio único que se generó a partir de una reserva. */
    @Override
    public Folio findByReservationId(Integer reservationId) {
        return folioRepository.findByReservationReservationId(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("The reservation folio does not exist."));
    }

    /** Verifica el folio y devuelve sus cargos ordenados cronológicamente. */
    @Override
    public List<FolioItem> listItemsByFolioId(Integer folioId) {
        findById(folioId);
        return folioItemRepository.findByFolioFolioIdOrderByChargedAtAsc(folioId);
    }

    /** Base imponible: coste del alojamiento mas los cargos de servicios. */
    @Override
    public BigDecimal calculateSubtotal(Integer folioId) {
        Folio folio = findById(folioId);
        return stayTotal(folio.getReservation())
                .add(servicesTotal(folioItemRepository.findByFolioFolioIdOrderByChargedAtAsc(folioId)));
    }

    /** Impuestos sobre la base imponible. */
    @Override
    public BigDecimal calculateTaxes(Integer folioId) {
        return calculateTaxesFor(calculateSubtotal(folioId));
    }

    /** Importe final que deben cubrir los pagos. */
    @Override
    public BigDecimal calculateTotal(Integer folioId) {
        return calculateTotalFor(calculateSubtotal(folioId));
    }

    /** Impuestos sobre una base imponible todavía sin folio. */
    @Override
    public BigDecimal calculateTaxesFor(BigDecimal subtotal) {
        if (subtotal == null) {
            return BigDecimal.ZERO;
        }
        return subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    /** Importe final sobre una base imponible todavía sin folio. */
    @Override
    public BigDecimal calculateTotalFor(BigDecimal subtotal) {
        if (subtotal == null) {
            return BigDecimal.ZERO;
        }
        return subtotal.add(calculateTaxesFor(subtotal));
    }

    /**
     * Importe de cada cargo, indexado por su identificador.
     *
     * Se devuelve como mapa porque la vista recorre los cargos guardados y
     * necesita acompanar cada fila con un importe que no esta en la tabla.
     */
    @Override
    public Map<Integer, BigDecimal> calculateItemSubtotals(Integer folioId) {
        Map<Integer, BigDecimal> subtotals = new LinkedHashMap<>();
        for (FolioItem item : folioItemRepository.findByFolioFolioIdOrderByChargedAtAsc(folioId)) {
            subtotals.put(item.getItemId(), itemSubtotal(item));
        }
        return subtotals;
    }

    /** Coste del alojamiento: tarifa pactada por noche x noches de estancia. */
    private BigDecimal stayTotal(Reservation reservation) {
        if (reservation == null || reservation.getNightlyPrice() == null
                || reservation.getCheckInDate() == null || reservation.getCheckOutDate() == null) {
            return BigDecimal.ZERO;
        }
        long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        return reservation.getNightlyPrice().multiply(BigDecimal.valueOf(nights));
    }

    /** Suma de los cargos de servicios de un folio. */
    private BigDecimal servicesTotal(List<FolioItem> items) {
        return items.stream()
                .map(this::itemSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Importe de un cargo: precio unitario x cantidad. */
    private BigDecimal itemSubtotal(FolioItem item) {
        if (item.getUnitPrice() == null || item.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}
