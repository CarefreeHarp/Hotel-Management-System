package com.example.demo.service;

import com.example.demo.entities.RoomType;
import com.example.demo.errors.InvalidRoomTypeDataException;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.repository.RoomTypeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los tipos de habitación.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 *
 * El repositorio es un RoomTypeRepository de Spring Data JPA, así que el
 * catálogo se guarda en la base de datos H2. Las validaciones y los mensajes de
 * error viven aquí, expresados con las excepciones propias del proyecto:
 *
 * - ResourceNotFoundException    -> el tipo buscado no está en el catálogo.
 * - InvalidRoomTypeDataException -> el formulario trae un dato inválido.
 */
@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    public RoomTypeRepository typeRoomRepository;

    @Override
    public List<RoomType> listTypes() {
        return typeRoomRepository.findAll();
    }

    @Override
    public RoomType findByName(String name) {
        return typeRoomRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No room type named " + name + " exists."));
    }

    @Override
    public RoomType findById(int roomTypeId) {
        return typeRoomRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No room type with the id " + roomTypeId + " exists."));
    }

    @Override
    public void create(RoomType typeRoom) {
        // El id lo genera la base de datos (IDENTITY): se manda en null para que
        // Hibernate haga un INSERT. El formulario nunca lo envía.
        typeRoom.setRoomTypeId(null);

        validateData(typeRoom);
        typeRoomRepository.save(typeRoom);
    }

    @Override
    public void update(String currentName, RoomType typeRoom) {
        RoomType typeRegistered = findByName(currentName);

        // Se conserva el id que ya tenía el tipo para que save() actualice esa
        // fila en vez de crear un registro nuevo.
        typeRoom.setRoomTypeId(typeRegistered.getRoomTypeId());

        validateData(typeRoom);
        typeRoomRepository.save(typeRoom);
    }

    @Override
    public void delete(String name) {
        RoomType typeRegistered = findByName(name);
        typeRoomRepository.deleteById(typeRegistered.getRoomTypeId());
    }

    /**
     * Reglas del negocio: el name y la description son obligatorios y no pueden
     * pasarse del largo de su columna, el name no se puede repetir, el price por
     * noche no puede ser negativo y la habitación tiene que recibir al menos a
     * una persona.
     *
     * El name se compara contra el id porque, al editar, el propio tipo conserva
     * su name y eso no debe contar como duplicado. Los ids se comparan con
     * Objects.equals y no con != porque son Integer (un objeto), y en un tipo
     * nuevo el id todavía viene en null.
     *
     * @throws InvalidRoomTypeDataException con el mensaje del primer dato inválido.
     */
    private void validateData(RoomType typeRoom) {
        if (typeRoom.getName() == null || typeRoom.getName().isBlank()) {
            throw new InvalidRoomTypeDataException(
                    InvalidRoomTypeDataException.Reason.NAME_REQUIRED, typeRoom.getName());
        }

        if (typeRoom.getName().length() > 50) {
            throw new InvalidRoomTypeDataException(
                    InvalidRoomTypeDataException.Reason.NAME_TOO_LONG, typeRoom.getName());
        }

        if (typeRoom.getDescription() == null || typeRoom.getDescription().isBlank()) {
            throw new InvalidRoomTypeDataException(
                    InvalidRoomTypeDataException.Reason.DESCRIPTION_REQUIRED, typeRoom.getDescription());
        }

        if (typeRoom.getDescription().length() > 500) {
            throw new InvalidRoomTypeDataException(
                    InvalidRoomTypeDataException.Reason.DESCRIPTION_TOO_LONG, typeRoom.getDescription());
        }

        RoomType typeWithThatName = typeRoomRepository.findByNameIgnoreCase(typeRoom.getName()).orElse(null);
        if (typeWithThatName != null
                && !Objects.equals(typeWithThatName.getRoomTypeId(), typeRoom.getRoomTypeId())) {
            throw new InvalidRoomTypeDataException(
                    InvalidRoomTypeDataException.Reason.NAME_ALREADY_EXISTS, typeRoom.getName());
        }

        if (typeRoom.getNightlyPrice() == null || typeRoom.getNightlyPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRoomTypeDataException(
                    InvalidRoomTypeDataException.Reason.NIGHTLY_PRICE_INVALID, typeRoom.getNightlyPrice());
        }

        if (typeRoom.getMaxCapacity() == null || typeRoom.getMaxCapacity() < 1) {
            throw new InvalidRoomTypeDataException(
                    InvalidRoomTypeDataException.Reason.MAX_CAPACITY_INVALID, typeRoom.getMaxCapacity());
        }
    }
}
