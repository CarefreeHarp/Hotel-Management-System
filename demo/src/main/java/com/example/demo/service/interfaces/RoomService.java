package com.example.demo.service.interfaces;

import com.example.demo.entities.Room;
import com.example.demo.errors.InvalidRoomDataException;
import com.example.demo.errors.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;

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

    /** Pagina habitaciones sin reservas vigentes que se crucen con las fechas solicitadas. */
    Page<Room> listAvailableForStay(LocalDate checkInDate, LocalDate checkOutDate, int page);

    /** Sincroniza el estado de ocupación según folios pagados y fechas de estadía. */
    void synchronizeOccupancyStatus();

    /** Devuelve la habitación asociada al identificador interno indicado. */
    Room findById(int roomId);

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
