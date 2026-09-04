package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import com.example.demo.entities.enums.RoomStatus;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "main_photo", nullable = false, length = 500)
    private String mainPhoto; // URL obligatoria de la imagen principal de la habitación.
    @ElementCollection
    @CollectionTable(name = "room_secondary_photo", joinColumns = @JoinColumn(name = "room_number"))
    @Column(name = "photo_url", nullable = false, length = 500)
    @Builder.Default
    private List<String> secondaryPhotos = new ArrayList<>(); // URLs de las imágenes secundarias de la habitación.
}
