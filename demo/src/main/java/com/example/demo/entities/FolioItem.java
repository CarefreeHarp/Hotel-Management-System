package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "folio_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"folio", "service"})
public class FolioItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folio_item_id")
    private Integer itemId; // Identificador del ítem del folio.
    @ManyToOne(optional = false)
    @JoinColumn(name = "folio_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Folio folio; // Folio al que pertenece el cargo.
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service; // Servicio facturado, si aplica.
    @Column(nullable = false, length = 150)
    private String concept; // Descripción del cargo facturado.
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // Precio unitario cobrado.
    @Column(nullable = false)
    private Integer quantity; // Cantidad de unidades cobradas.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal; // Monto antes de impuestos.
    @Column(name = "charged_at", nullable = false)
    private LocalDateTime chargedAt; // Fecha y hora en que se registró el cargo.
}
