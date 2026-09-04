package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

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
}
