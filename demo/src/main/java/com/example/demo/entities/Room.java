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
    @Column(name = "main_photo", nullable = false, length = 500)
    private String mainPhoto; // URL obligatoria de la imagen principal de la habitación.
    @ElementCollection
    @CollectionTable(name = "room_secondary_photo", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "photo_url", nullable = false, length = 500)
    @Builder.Default
    private List<String> secondaryPhotos = new ArrayList<>(); // URLs de las imágenes secundarias de la habitación.

    /** Crea una habitación nueva; la base de datos genera su identificador. */
    public Room(Integer number,
                Integer floor,
                RoomStatus status,
                RoomType roomType,
                String mainPhoto,
                List<String> secondaryPhotos) {
        this.number = number;
        this.floor = floor;
        this.status = status;
        this.roomType = roomType;
        this.mainPhoto = mainPhoto;
        this.secondaryPhotos = secondaryPhotos == null
                ? new ArrayList<>()
                : new ArrayList<>(secondaryPhotos);
    }
}
