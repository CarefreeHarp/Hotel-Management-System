package com.example.demo.service;

import com.example.demo.entitys.TipoHabitacion;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * CAPA DE SERVICIO: lógica de negocio de los tipos de habitación.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Al tipo de habitación se le identifica por su nombre (Normal, Executive, VIP,
 * Luxury) y no por su id, porque el id es interno de la base de datos y no se
 * muestra en los formularios. El nombre es único.
 *
 * MANEJO DE ERRORES: el servicio valida las reglas del negocio y avisa del error
 * lanzando una excepción genérica con un mensaje personalizado:
 *
 * - NoSuchElementException   -> el tipo de habitación buscado no existe.
 * - IllegalArgumentException -> los datos del formulario no son válidos.
 */
public interface TipoHabitacionService {

    /** Lista todos los tipos de habitación del hotel. */
    List<TipoHabitacion> listarTipos();

    /**
     * Devuelve el tipo de habitación con ese nombre.
     *
     * @throws NoSuchElementException si no existe un tipo con ese nombre.
     */
    TipoHabitacion buscarPorNombre(String nombre);

    /**
     * Devuelve el tipo de habitación con ese id.
     *
     * @throws NoSuchElementException si no existe un tipo con ese id.
     */
    TipoHabitacion buscarPorId(int idTipo);

    /**
     * Crea un tipo de habitación validando que el nombre no esté repetido.
     *
     * @throws IllegalArgumentException si los datos no cumplen las reglas del negocio.
     */
    void crear(TipoHabitacion tipoHabitacion);

    /**
     * Actualiza un tipo de habitación existente.
     * nombreActual identifica el tipo que se está editando, porque el
     * administrador puede estar cambiando justamente su nombre.
     *
     * @throws NoSuchElementException   si no existe un tipo llamado nombreActual.
     * @throws IllegalArgumentException si los datos nuevos no son válidos.
     */
    void actualizar(String nombreActual, TipoHabitacion tipoHabitacion);

    /**
     * Elimina del catálogo el tipo de habitación con ese nombre.
     *
     * @throws NoSuchElementException si no existe un tipo con ese nombre.
     */
    void eliminar(String nombre);
}
