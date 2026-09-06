package com.example.demo.service;

import com.example.demo.entities.RoomType;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * CAPA DE SERVICIO: lógica de negocio de los tipos de habitación.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Al tipo de habitación se le identifica por su name (Normal, Executive, VIP,
 * Luxury) y no por su id, porque el id es interno de la base de datos y no se
 * muestra en los formularios. El name es único.
 *
 * MANEJO DE ERRORES: el servicio valida las reglas del negocio y avisa del error
 * lanzando una excepción genérica con un mensaje personalizado:
 *
 * - NoSuchElementException   -> el tipo de habitación buscado no existe.
 * - IllegalArgumentException -> los datos del formulario no son válidos.
 */
public interface RoomTypeService {

    /** Lista todos los tipos de habitación del hotel. */
    List<RoomType> listTypes();

    /**
     * Devuelve el tipo de habitación con ese name.
     *
     * @throws NoSuchElementException si no existe un tipo con ese name.
     */
    RoomType findByName(String name);

    /**
     * Devuelve el tipo de habitación con ese id.
     *
     * @throws NoSuchElementException si no existe un tipo con ese id.
     */
    RoomType findById(int roomTypeId);

    /**
     * Crea un tipo de habitación validando que el name no esté repetido.
     *
     * @throws IllegalArgumentException si los datos no cumplen las reglas del negocio.
     */
    void create(RoomType typeRoom);

    /**
     * Actualiza un tipo de habitación existente.
     * nombreActual identifica el tipo que se está editando, porque el
     * administrador puede estar cambiando justamente su name.
     *
     * @throws NoSuchElementException   si no existe un tipo llamado nombreActual.
     * @throws IllegalArgumentException si los datos nuevos no son válidos.
     */
    void update(String currentName, RoomType typeRoom);

    /**
     * Elimina del catálogo el tipo de habitación con ese name.
     *
     * @throws NoSuchElementException si no existe un tipo con ese name.
     */
    void delete(String name);
}
