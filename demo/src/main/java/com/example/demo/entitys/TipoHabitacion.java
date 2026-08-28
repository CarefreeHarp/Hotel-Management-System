package com.example.demo.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tipo de habitación del hotel (Normal, Executive, VIP, Luxury).
 * Lo administra el administrador y clasifica a las habitaciones:
 * varias habitaciones comparten el mismo tipo.
 *
 * El nombre es único y la descripción es obligatoria, porque es la que se
 * le muestra al cliente cuando escoge el tipo de habitación que quiere reservar.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoHabitacion {

    private int idTipo;
    private String nombre;
    private String descripcion;
    private double precioNoche;
    private int capacidadMaxima;

}
