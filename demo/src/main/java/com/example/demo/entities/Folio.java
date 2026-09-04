package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import com.example.demo.entities.enums.FolioStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "folio")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "reservation")
public class Folio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folio_id")
    private Integer folioId; // Identificador del folio.
    @OneToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reservation reservation; // Reserva vinculada a este folio.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; // Monto antes de impuestos.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal taxes; // Valor de los impuestos aplicados.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total; // Monto final con impuestos.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FolioStatus status; // Estado actual del registro.
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt; // Fecha y hora de emisión del folio.
}
