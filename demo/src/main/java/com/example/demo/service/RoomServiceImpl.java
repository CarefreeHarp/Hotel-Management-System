package com.example.demo.service;

import com.example.demo.entities.Room;
import com.example.demo.errors.InvalidRoomDataException;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.repository.RoomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Room> listRooms() {
        return roomRepository.findByRoomIdNotOrderByNumberAsc(-1);
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
        normalizePhotos(room);
        validateData(room, 0);
        roomRepository.save(room);
    }

    @Override
    public void update(int numberCurrent, Room room) {
        Room registeredRoom = findByNumber(numberCurrent);

        normalizePhotos(room);
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

        if (room.getMainPhoto() == null || room.getMainPhoto().isBlank()) {
            throw new InvalidRoomDataException(
                    InvalidRoomDataException.Reason.MAIN_PHOTO_REQUIRED, room.getNumber());
        }
    }

    /**
     * Limpia los espacios de la foto principal y descarta las filas de fotos
     * secundarias que el administrador dejó vacías en el formulario, para que no
     * se guarden URLs en blanco en la tabla ROOM_SECONDARY_PHOTO.
     */
    private void normalizePhotos(Room room) {
        if (room.getMainPhoto() != null) {
            room.setMainPhoto(room.getMainPhoto().trim());
        }

        if (room.getSecondaryPhotos() != null) {
            room.getSecondaryPhotos().removeIf(photo -> photo == null || photo.isBlank());
        }
    }
}
