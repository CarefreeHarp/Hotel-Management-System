package com.example.demo.service;

import com.example.demo.entitys.TipoHabitacion;
import java.util.List;

/**
 * CAPA DE SERVICIO: lógica de negocio de los tipos de habitación.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Al tipo de habitación se le identifica por su nombre (Normal, Executive, VIP,
 * Luxury) y no por su id, porque el id es interno de la base de datos y no se
 * muestra en los formularios. El nombre es único.
 *
 * Los métodos que modifican datos devuelven un String: null cuando la operación
 * salió bien, o el mensaje de error que se le debe mostrar al administrador cuando no.
 */
public interface TipoHabitacionService {

    /** Lista todos los tipos de habitación del hotel. */
    List<TipoHabitacion> listarTipos();

    /** Devuelve el tipo con ese nombre, o null si no existe. */
    TipoHabitacion buscarPorNombre(String nombre);

    /** Crea un tipo de habitación validando que el nombre no esté repetido. */
    String crear(TipoHabitacion tipoHabitacion);

    /**
     * Actualiza un tipo de habitación existente.
     * nombreActual identifica el tipo que se está editando, porque el
     * administrador puede estar cambiando justamente su nombre.
     */
    String actualizar(String nombreActual, TipoHabitacion tipoHabitacion);

    /** Elimina del catálogo el tipo de habitación con ese nombre. */
    String eliminar(String nombre);
}
