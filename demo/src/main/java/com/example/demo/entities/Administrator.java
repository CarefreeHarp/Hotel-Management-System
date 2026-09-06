package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "administrator")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer adminId; // Identificador del administrador.
    @Column(nullable = false, length = 60)
    private String name; // Nombre del registro.
    @Column(nullable = false, unique = true, length = 100)
    private String email; // Correo electrónico único de contacto e inicio de sesión.
    @Column(nullable = false, length = 255)
    private String password; // Contraseña de la cuenta.

    /** Crea un administrador nuevo; la base de datos genera su identificador. */
    public Administrator(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
