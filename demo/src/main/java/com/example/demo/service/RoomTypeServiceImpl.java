package com.example.demo.service;

import com.example.demo.entities.RoomType;
import com.example.demo.repository.RoomTypeInMemoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los tipos de habitación.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 *
 * Las validaciones y los mensajes de error viven aquí: el controlador solo
 * atrapa la excepción y decide a qué pantalla lleva.
 */
@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    public RoomTypeInMemoryRepository typeRoomRepository;

    @Override
    public List<RoomType> listTypes() {
        return typeRoomRepository.listAll();
    }

    @Override
    public RoomType findByName(String name) {
        RoomType typeRoom = findType(name);
        if (typeRoom == null) {
            throw new NoSuchElementException("No room type named " + name + " exists.");
        }

        return typeRoom;
    }

    @Override
    public RoomType findById(int roomTypeId) {
        RoomType typeRoom = typeRoomRepository.findById(roomTypeId);
        if (typeRoom == null) {
            throw new NoSuchElementException("No room type with the id " + roomTypeId + " exists.");
        }

        return typeRoom;
    }

    @Override
    public void create(RoomType typeRoom) {
        // El id lo genera el repositorio: el formulario nunca lo envía.
        typeRoom.setRoomTypeId(0);

        validateData(typeRoom);
        typeRoomRepository.save(typeRoom);
    }

    @Override
    public void update(String currentName, RoomType typeRoom) {
        RoomType typeRegistered = findByName(currentName);

        // Se conserva el id que ya tenía el tipo para no crear un registro nuevo.
        typeRoom.setRoomTypeId(typeRegistered.getRoomTypeId());

        validateData(typeRoom);
        typeRoomRepository.save(typeRoom);
    }

    @Override
    public void delete(String name) {
        RoomType typeRegistered = findByName(name);
        typeRoomRepository.delete(typeRegistered.getRoomTypeId());
    }

    /**
     * Búsqueda interna que sí puede devolver null, porque las validaciones
     * necesitan preguntar si un name ya está usado sin que eso sea un error.
     */
    private RoomType findType(String name) {
        return typeRoomRepository.listAll()
                .stream()
                .filter(typeRoom -> typeRoom.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Reglas del negocio: el name no se puede repetir, el price por noche no
     * puede ser negativo y la habitación tiene que recibir al menos a una persona.
     * El name se compara contra el id porque, al editar, el propio tipo conserva
     * su name y eso no debe contar como duplicado.
     *
     * @throws IllegalArgumentException con el mensaje del primer dato inválido.
     */
    private void validateData(RoomType typeRoom) {
        RoomType typeWithThatName = findType(typeRoom.getName());
        if (typeWithThatName != null && typeWithThatName.getRoomTypeId() != typeRoom.getRoomTypeId()) {
            throw new IllegalArgumentException(
                    "A room type named " + typeRoom.getName() + " already exists.");
        }

        if (typeRoom.getNightlyPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The nightly price cannot be negative.");
        }

        if (typeRoom.getMaxCapacity() < 1) {
            throw new IllegalArgumentException("Maximum capacity must be at least one guest.");
        }
    }
}
