package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import com.example.demo.entities.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @ManyToOne
    @JoinColumn(name = "client_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Client client; // Client que realizó la reserva.
    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    @ColumnDefault("-1")
    @OnDelete(action = OnDeleteAction.SET_DEFAULT)
    private Room room; // Habitación asignada a la reserva.

    /** Crea una reserva nueva; la base de datos genera su identificador. */
    public Reservation(String reservationCode,
                       LocalDate checkInDate,
                       LocalDate checkOutDate,
                       Integer guestCount,
                       BigDecimal nightlyPrice,
                       BigDecimal estimatedTotal,
                       ReservationStatus status,
                       LocalDateTime createdAt,
                       Client client,
                       Room room) {
        this.reservationCode = reservationCode;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestCount = guestCount;
        this.nightlyPrice = nightlyPrice;
        this.estimatedTotal = estimatedTotal;
        this.status = status;
        this.createdAt = createdAt;
        this.client = client;
        this.room = room;
    }
}
