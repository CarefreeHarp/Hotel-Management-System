package com.example.demo.service;

import com.example.demo.entities.Room;
import com.example.demo.repository.RoomInMemoryRepository;
import com.example.demo.repository.RoomTypeInMemoryRepository;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Valida los datos de la habitación antes de guardarla en el repositorio en memoria.
 * Cuando un dato no cumple una regla del negocio se lanza una excepción con el
 * mensaje que verá el administrador.
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomInMemoryRepository roomRepository;

    @Autowired
    RoomTypeInMemoryRepository typeRoomRepository;

    @Override
    public List<Room> listRooms() {
        return roomRepository.listAll();
    }

    @Override
    public Room findByNumber(int number) {
        Room room = roomRepository.findByNumber(number);
        if (room == null) {
            throw new NoSuchElementException("The room " + number + " does not exist.");
        }

        return room;
    }

    @Override
    public void create(Room room) {
        validateData(room, 0);
        roomRepository.save(room);
    }

    @Override
    public void update(int numberCurrent, Room room) {
        // Solo se comprueba que exista: si no, buscarPorNumero lanza la excepción.
        findByNumber(numberCurrent);

        validateData(room, numberCurrent);

        if (numberCurrent != room.getNumber()) {
            roomRepository.delete(numberCurrent);
        }
        roomRepository.save(room);
    }

    @Override
    public void delete(int number) {
        if (!roomRepository.delete(number)) {
            throw new NoSuchElementException("The room " + number + " does not exist.");
        }
    }

    /**
     * Reglas del negocio de la habitación.
     * Se consulta el repositorio directamente y no buscarPorNumero porque aquí
     * preguntar si el número ya está usado no es un error, es parte de la validación.
     *
     * @throws IllegalArgumentException con el mensaje del primer dato inválido.
     */
    private void validateData(Room room, int numberCurrent) {
        normalizarPhotos(room);

        if (room.getNumber() < 1) {
            throw new IllegalArgumentException("The room number must be greater than zero.");
        }

        Room withThatNumber = roomRepository.findByNumber(room.getNumber());
        if (withThatNumber != null && room.getNumber() != numberCurrent) {
            throw new IllegalArgumentException("A room with that number already exists.");
        }

        if (room.getFloor() < 0) {
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

    private void normalizarPhotos(Room room) {
        if (room.getMainPhoto() != null) {
            room.setMainPhoto(room.getMainPhoto().trim());
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
