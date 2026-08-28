package com.example.demo.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cliente del hotel. Se registra por su cuenta desde el portal público
 * y es quien realiza las reservas.
 *
 * La cédula y el correo son únicos: no puede haber dos clientes con los mismos.
 * El correo además hace las veces de usuario para el login.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    private int idCliente;
    private String nombre;
    private String apellido;
    private String cedula;
    private String telefono;
    private String correo;
    private String password;

}
