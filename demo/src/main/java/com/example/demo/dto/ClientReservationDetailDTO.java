package com.example.demo.dto;

import com.example.demo.entities.Folio;
import com.example.demo.entities.FolioItem;
import com.example.demo.entities.Payment;
import com.example.demo.entities.Reservation;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
}
