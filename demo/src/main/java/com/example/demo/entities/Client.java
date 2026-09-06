package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer clientId; // Identificador del cliente.
    @Column(nullable = false, length = 60)
    private String name; // Nombre del registro.
    @Column(nullable = false, length = 60)
    private String lastName; // Apellido de la persona.
    @Column(nullable = false, unique = true, length = 20)
    private String nationalId; // Documento de identidad único del cliente.
    @Column(nullable = false, length = 20)
    private String phone; // Teléfono de contacto.
    @Column(nullable = false, unique = true, length = 100)
    private String email; // Correo electrónico único de contacto e inicio de sesión.
    @Column(nullable = false, length = 255)
    private String password; // Contraseña de la cuenta.
    @Column(name = "profile_photo", length = 500)
    private String profilePhoto; // URL opcional de la foto de perfil.
}
