package com.example.demo.service;

import com.example.demo.entities.RoomType;
import com.example.demo.errors.InvalidRoomTypeDataException;
import com.example.demo.errors.RoomTypeNotFoundException;
import java.util.List;

/**
 * CAPA DE SERVICIO: lógica de negocio de los tipos de habitación.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Al tipo de habitación se le identifica por su name (Normal, Executive, VIP,
 * Luxury) y no por su id, porque el id es interno de la base de datos y no se
 * muestra en los formularios. El name es único.
 *
 * MANEJO DE ERRORES: el servicio valida las reglas del negocio y avisa del error
 * lanzando una de las excepciones propias del proyecto:
 *
 * - RoomTypeNotFoundException    -> el tipo de habitación buscado no existe.
 * - InvalidRoomTypeDataException -> los datos del formulario no son válidos.
 */
public interface RoomTypeService {

    /** Lista todos los tipos de habitación del hotel. */
    List<RoomType> listTypes();

    /**
     * Devuelve el tipo de habitación con ese name.
     *
     * @throws RoomTypeNotFoundException si no existe un tipo con ese name.
     */
    RoomType findByName(String name);

    /**
     * Devuelve el tipo de habitación con ese id.
     *
     * @throws RoomTypeNotFoundException si no existe un tipo con ese id.
     */
    RoomType findById(int roomTypeId);

    /**
     * Crea un tipo de habitación validando que el name no esté repetido.
     *
     * @throws InvalidRoomTypeDataException si los datos no cumplen las reglas del negocio.
     */
    void create(RoomType typeRoom);

    /**
     * Actualiza un tipo de habitación existente.
     * currentName identifica el tipo que se está editando, porque el
     * administrador puede estar cambiando justamente su name.
     *
     * @throws RoomTypeNotFoundException    si no existe un tipo llamado currentName.
     * @throws InvalidRoomTypeDataException si los datos nuevos no son válidos.
     */
    void update(String currentName, RoomType typeRoom);

    /**
     * Elimina del catálogo el tipo de habitación con ese name.
     *
     * @throws RoomTypeNotFoundException si no existe un tipo con ese name.
     */
    void delete(String name);
}
