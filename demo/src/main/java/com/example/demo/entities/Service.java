package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer serviceId; // Identificador del servicio.
    @Column(nullable = false, length = 100)
    private String name; // Nombre del registro.
    @Column(name = "url_name", nullable = false, unique = true, length = 100)
    private String urlName; // Nombre único apto para URL.
    @Lob
    @Column(nullable = false)
    private String description; // Descripción detallada.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Precio del servicio.
    @Column(nullable = false, length = 50)
    private String category; // Categoría del servicio.
    @Column(nullable = false)
    private Boolean active; // Indica si el servicio está disponible.
    @Column(length = 255)
    private String summary; // Resumen corto opcional.
    @Column(length = 50)
    private String duration; // Duración opcional del servicio.
    @Column(length = 100)
    private String availability; // Información opcional de disponibilidad.
    @Column(length = 100)
    private String location; // Ubicación opcional del servicio.
    @Column(nullable = false, name = "main_image_url", length = 500)
    private String mainImageUrl; // URL opcional de la imagen principal del servicio.
    @ElementCollection
    @CollectionTable(name = "service_secondary_image", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "image_url", nullable = false, length = 500)
    private List<String> secondaryImageUrls = new ArrayList<>(); // URLs de las imágenes secundarias del servicio.

    /** Crea un servicio nuevo; la base de datos genera su identificador. */
    public Service(String name,
                   String urlName,
                   String description,
                   BigDecimal price,
                   String category,
                   Boolean active,
                   String summary,
                   String duration,
                   String availability,
                   String location,
                   String mainImageUrl) {
        this.name = name;
        this.urlName = urlName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.active = active;
        this.summary = summary;
        this.duration = duration;
        this.availability = availability;
        this.location = location;
        this.mainImageUrl = mainImageUrl;
    }
}
