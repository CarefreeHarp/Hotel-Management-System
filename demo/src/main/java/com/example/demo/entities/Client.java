package com.example.demo.entities;

import jakarta.persistence.*;
import java.util.UUID;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "client_id")
    private UUID clientId; // Identificador público y único del cliente.
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

    /** Crea un cliente nuevo; la base de datos genera su identificador UUID. */
    public Client(String name,
                  String lastName,
                  String nationalId,
                  String phone,
                  String email,
                  String password,
                  String profilePhoto) {
        this.name = name;
        this.lastName = lastName;
        this.nationalId = nationalId;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.profilePhoto = profilePhoto;
    }
}
