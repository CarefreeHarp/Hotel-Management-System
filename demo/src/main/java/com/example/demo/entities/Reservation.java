package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import com.example.demo.entities.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"client", "room"})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId; // Identificador de la reserva.
    @Column(name = "reservation_code", nullable = false, unique = true, length = 30)
    private String reservationCode; // Código único de la reserva.
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate; // Fecha programada de llegada.
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate; // Fecha programada de salida.
    @Column(name = "guest_count", nullable = false)
    private Integer guestCount; // Cantidad de huéspedes de la reserva.
    @Column(name = "nightly_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal nightlyPrice; // Tarifa acordada por noche.
    @Column(name = "estimated_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedTotal; // Costo total estimado de la reserva.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status; // Estado actual del registro.
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Fecha y hora de creación.
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client; // Client que realizó la reserva.
    @ManyToOne(optional = false)
    @JoinColumn(name = "room_number", nullable = false)
    private Room room; // Habitación asignada a la reserva.
}
