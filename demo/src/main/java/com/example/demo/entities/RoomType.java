package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "room_type")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Integer roomTypeId; // Identificador del tipo de habitación.
    @Column(nullable = false, unique = true, length = 50)
    private String name; // Nombre del registro.
    @Column(nullable = false, length = 500)
    private String description; // Descripción detallada.
    @Column(name = "nightly_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal nightlyPrice; // Tarifa acordada por noche.
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity; // Capacidad máxima de huéspedes.
}
