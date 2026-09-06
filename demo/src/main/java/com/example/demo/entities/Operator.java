package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "operator")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "admin")
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operator_id")
    private Integer operatorId; // Identificador del operario.
    @Column(nullable = false, length = 60)
    private String name; // Nombre del registro.
    @Column(nullable = false, length = 60)
    private String lastName; // Apellido de la persona.
    @Column(nullable = false, unique = true, length = 100)
    private String email; // Correo electrónico único de contacto e inicio de sesión.
    @Column(nullable = false, length = 255)
    private String password; // Contraseña de la cuenta.
    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private Administrator admin; // Administrator responsable del operario.
}
