package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import com.example.demo.entities.enums.RoomStatus;

@Data
@Entity
@Table(name = "room")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "roomType")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId; // Identificador interno de la habitación.
    @Column(name = "room_number", nullable = false, unique = true)
    private Integer number; // Número físico único de la habitación.
    @Column(nullable = false)
    private Integer floor; // Piso donde se ubica la habitación.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoomStatus status; // Estado actual del registro.
    @ManyToOne(optional = false)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType; // Tipo de habitación asignado.

}
