package com.example.demo.service;

import com.example.demo.entities.Room;
import com.example.demo.entities.enums.FolioStatus;
import com.example.demo.entities.enums.ReservationStatus;
import com.example.demo.entities.enums.RoomStatus;
import com.example.demo.errors.InvalidRoomDataException;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.repository.RoomRepository;
import com.example.demo.service.interfaces.RoomService;
import java.time.LocalDate;
import java.time.Clock;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de la lógica de negocio de las habitaciones.
 *
 * El repositorio es ahora un RoomRepository de Spring Data JPA. Como el número
 * de la habitación es un dato único de negocio; la llave primaria interna es
 * room_id, generada por la base de datos.
 *
 * Cuando un dato no cumple una regla del negocio se lanza una excepción con el
 * mensaje que verá el administrador.
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    private Clock applicationClock;

    @Override
    public List<Room> listRooms() {
        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "number"));
    }

    @Override
    public Page<Room> listAvailableForStay(LocalDate checkInDate, LocalDate checkOutDate, int page) {
        if (checkInDate == null || checkOutDate == null || !checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out must be after check-in.");
        }
        return roomRepository.findAvailableForStay(checkInDate, checkOutDate, ReservationStatus.CANCELLED,
                RoomStatus.MAINTENANCE, PageRequest.of(Math.max(0, page), 5));
    }

    /** Actualiza todas las habitaciones operativas y luego ocupa las estadías pagadas vigentes. */
    @Override
    @Transactional
    public void synchronizeOccupancyStatus() {
        roomRepository.markRoomsAsAvailable(RoomStatus.AVAILABLE, RoomStatus.MAINTENANCE);
        roomRepository.markPaidStayRoomsAsOccupied(
                RoomStatus.OCCUPIED,
                RoomStatus.MAINTENANCE,
                FolioStatus.PAID,
                ReservationStatus.CANCELLED,
                LocalDate.now(applicationClock));
    }

    @Override
    public Room findById(int roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The requested room does not exist."));
    }

    @Override
    public Room findByNumber(int number) {
        return roomRepository.findByNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The room " + number + " does not exist."));
    }

    @Override
    public void create(Room room) {
        // El id lo genera la base de datos. El formulario nunca debe conservarlo.
        room.setRoomId(null);
        validateData(room, 0);
        roomRepository.save(room);
    }

    @Override
    public void update(int numberCurrent, Room room) {
        Room registeredRoom = findByNumber(numberCurrent);

        validateData(room, numberCurrent);

        // Se conserva el id para actualizar la misma fila si cambia el número.
        room.setRoomId(registeredRoom.getRoomId());
        roomRepository.save(room);
    }

    @Override
    public void delete(int number) {
        Room registeredRoom = findByNumber(number);
        roomRepository.deleteById(registeredRoom.getRoomId());
    }

    /**
     * Reglas del negocio de la habitación.
     * Se le pregunta al repositorio si el número ya está usado con existsByNumber,
     * porque aquí encontrarlo no es un error sino parte de la validación.
     *
     * Los campos numéricos se comparan después de descartar el null, porque son
     * objetos (Integer) y el formulario puede llegar sin ellos.
     *
     * @throws InvalidRoomDataException con el mensaje del primer dato inválido.
     */
    private void validateData(Room room, int numberCurrent) {
        if (room.getNumber() == null || room.getNumber() < 1) {
            throw new InvalidRoomDataException(
                    InvalidRoomDataException.Reason.ROOM_NUMBER_MUST_BE_POSITIVE, room.getNumber());
        }

        if (room.getNumber() != numberCurrent && roomRepository.existsByNumber(room.getNumber())) {
            throw new InvalidRoomDataException(
                    InvalidRoomDataException.Reason.ROOM_NUMBER_ALREADY_EXISTS, room.getNumber());
        }

        if (room.getFloor() == null || room.getFloor() < 0) {
            throw new InvalidRoomDataException(
                    InvalidRoomDataException.Reason.FLOOR_CANNOT_BE_NEGATIVE, room.getFloor());
        }

        if (room.getStatus() == null) {
            throw new InvalidRoomDataException(
                    InvalidRoomDataException.Reason.STATUS_REQUIRED, room.getNumber());
        }

        if (room.getRoomType() == null) {
            throw new InvalidRoomDataException(
                    InvalidRoomDataException.Reason.ROOM_TYPE_REQUIRED, room.getNumber());
        }

    }
}
