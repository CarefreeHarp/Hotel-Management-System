package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import com.example.demo.entities.enums.PaymentStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"folio", "operator"})
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId; // Identificador del pago.
    @ManyToOne(optional = false)
    @JoinColumn(name = "folio_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Folio folio; // Folio al que pertenece el pago.
    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operator operator; // Operario que procesó el pago, si aplica.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount; // Monto recibido en el pago.
    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod; // Método de pago utilizado.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status; // Estado actual del registro.
    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt; // Fecha y hora de registro del pago.
}
