package com.example.demo.dto;

import com.example.demo.entities.Folio;
import com.example.demo.entities.FolioItem;
import com.example.demo.entities.Payment;
import com.example.demo.entities.Reservation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Encapsula la información completa de una reserva junto con su folio,
 * servicios contratados y registros de pago para vistas de cliente y staff.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientReservationDetailDTO {
    private Reservation reservation;
    private Folio folio;
    @Builder.Default
    private List<FolioItem> folioItems = Collections.emptyList();
    @Builder.Default
    private List<Payment> payments = Collections.emptyList();

    // Importes calculados por FolioService: no existen como columnas y por eso
    // viajan hasta la vista dentro de este objeto de transporte.
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal taxes = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;
    /** Importe de cada cargo, indexado por el identificador del cargo. */
    @Builder.Default
    private Map<Integer, BigDecimal> itemSubtotals = Collections.emptyMap();

    public boolean hasPayments() {
        return payments != null && !payments.isEmpty();
    }

    public Payment getPrimaryPayment() {
        return hasPayments() ? payments.get(0) : null;
    }

    public String getPrimaryPaymentMethod() {
        Payment primary = getPrimaryPayment();
        return primary != null ? primary.getPaymentMethod() : "Pending";
    }

    public LocalDateTime getPrimaryPaymentDate() {
        Payment primary = getPrimaryPayment();
        return primary != null ? primary.getPaidAt() : null;
    }

    public long getNights() {
        if (reservation == null || reservation.getCheckInDate() == null || reservation.getCheckOutDate() == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
    }
}
