package com.example.demo.service;

import com.example.demo.entities.Room;
import com.example.demo.errors.InvalidRoomDataException;
import com.example.demo.errors.ResourceNotFoundException;
import java.util.List;

/**
 * CAPA DE SERVICIO: lógica de negocio del catálogo de habitaciones.
 *
 * MANEJO DE ERRORES: las reglas del negocio se validan aquí y el error se
 * comunica lanzando una excepción personalizada con un mensaje:
 *
 * - ResourceNotFoundException -> la habitación buscada no existe.
 * - InvalidRoomDataException -> los datos del formulario no son válidos.
 */
public interface RoomService {

    List<Room> listRooms();

    /**
     * Devuelve la habitación con ese número.
     *
     * @throws ResourceNotFoundException si no existe una habitación con ese número.
     */
    Room findByNumber(int number);

    /**
     * Registra una habitación nueva.
     *
     * @throws InvalidRoomDataException si los datos no cumplen las reglas del negocio.
     */
    void create(Room room);

    /**
     * Actualiza una habitación existente. numeroActual es el número que tenía
     * antes de editarla, porque el administrador puede estar cambiándolo.
     *
     * @throws ResourceNotFoundException si no existe la habitación numeroActual.
     * @throws InvalidRoomDataException si los datos nuevos no son válidos.
     */
    void update(int numberCurrent, Room room);

    /**
     * Elimina la habitación con ese número.
     *
     * @throws ResourceNotFoundException si no existe una habitación con ese número.
     */
    void delete(int number);
}
