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
    @Column(name = "room_number")
    private Integer number; // Número físico de la habitación.
    @Column(nullable = false)
    private Integer floor; // Piso donde se ubica la habitación.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoomStatus status; // Estado actual del registro.
    @ManyToOne(optional = false)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType; // Tipo de habitación asignado.
    @Column(name = "main_photo", length = 500)
    private String mainPhoto; // URL opcional de la imagen principal de la habitación.
}
