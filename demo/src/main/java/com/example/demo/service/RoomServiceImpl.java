package com.example.demo.service;

import com.example.demo.entities.Room;
import com.example.demo.repository.RoomRepository;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de las habitaciones.
 *
 * El repositorio es ahora un RoomRepository de Spring Data JPA. Como el número
 * de la habitación es la llave primaria de la tabla ROOM, buscar por número es
 * directamente el findById que se hereda de JpaRepository.
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
        return roomRepository.findAll();
    }

    @Override
    public Room findByNumber(int number) {
        return roomRepository.findById(number)
                .orElseThrow(() -> new NoSuchElementException("The room " + number + " does not exist."));
    }

    @Override
    public void create(Room room) {
        // Se valida contra el número 0, que ninguna habitación puede tener, para
        // que cualquier número ya usado cuente como duplicado.
        validateData(room, 0);
        roomRepository.save(room);
    }

    @Override
    public void update(int numberCurrent, Room room) {
        // Solo se comprueba que exista: si no, findByNumber lanza la excepción.
        findByNumber(numberCurrent);

        validateData(room, numberCurrent);

        // El número es la llave primaria, así que cambiarlo no es un UPDATE: hay
        // que borrar la fila vieja y guardar la habitación con su número nuevo.
        if (numberCurrent != room.getNumber()) {
            roomRepository.deleteById(numberCurrent);
        }
        roomRepository.save(room);
    }

    @Override
    public void delete(int number) {
        if (!roomRepository.existsById(number)) {
            throw new NoSuchElementException("The room " + number + " does not exist.");
        }

        roomRepository.deleteById(number);
    }

    /**
     * Reglas del negocio de la habitación.
     * Se le pregunta al repositorio si el número ya está usado con existsById,
     * porque aquí encontrarlo no es un error sino parte de la validación.
     *
     * Los campos numéricos se comparan después de descartar el null, porque son
     * objetos (Integer) y el formulario puede llegar sin ellos.
     *
     * @throws IllegalArgumentException con el mensaje del primer dato inválido.
     */
    private void validateData(Room room, int numberCurrent) {
        normalizarPhotos(room);

        if (room.getNumber() == null || room.getNumber() < 1) {
            throw new IllegalArgumentException("The room number must be greater than zero.");
        }

        if (room.getNumber() != numberCurrent && roomRepository.existsById(room.getNumber())) {
            throw new IllegalArgumentException("A room with that number already exists.");
        }

        if (room.getFloor() == null || room.getFloor() < 0) {
            throw new IllegalArgumentException("The floor cannot be negative.");
        }

        if (room.getStatus() == null) {
            throw new IllegalArgumentException("Select a room status.");
        }

        if (room.getRoomType() == null) {
            throw new IllegalArgumentException("Select an existing room type.");
        }

        if (!isValidHttpUrl(room.getMainPhoto())) {
            throw new IllegalArgumentException("The main room photo must be a valid HTTP or HTTPS URL.");
        }
    }

    /**
     * Limpia los espacios de la foto principal y descarta las filas de fotos
     * secundarias que el administrador dejó vacías en el formulario, para que no
     * se guarden URLs en blanco en la tabla ROOM_SECONDARY_PHOTO.
     */
    private void normalizarPhotos(Room room) {
        if (room.getMainPhoto() != null) {
            room.setMainPhoto(room.getMainPhoto().trim());
        }

        if (room.getSecondaryPhotos() != null) {
            room.getSecondaryPhotos().removeIf(photo -> photo == null || photo.isBlank());
        }
    }

    private boolean isValidHttpUrl(String url) {
        if (url == null || url.isBlank()) {
            return true;
        }

        try {
            URI uri = URI.create(url);
            return uri.isAbsolute() && ("http".equalsIgnoreCase(uri.getScheme())
                    || "https".equalsIgnoreCase(uri.getScheme()));
        } catch (IllegalArgumentException error) {
            return false;
        }
    }
}
