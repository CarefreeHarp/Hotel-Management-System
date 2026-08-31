package com.example.demo.entitys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Habitación física del hotel. La crea el administrador y le asigna un tipo.
 *
 * El número de la habitación es su identificador, no se genera automáticamente:
 * lo escribe el administrador porque corresponde al número real de la puerta.
 * idTipo es la llave foránea hacia {@link TipoHabitacion}.
 *
 * "disponible" indica si la habitación está habilitada para reservarse; se usa
 * para deshabilitarla temporalmente cuando está en reparación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Habitacion {

    private int numero;
    private int piso;
    private EstadoHabitacion estado;
    private boolean disponible;
    private int idTipo;
    /** HTTP(S) URL for the image shown as the room cover. */
    private String fotoPrincipal;
    /** Optional HTTP(S) URLs for the remaining room gallery images. */
    private List<String> fotos = new ArrayList<>();

}
