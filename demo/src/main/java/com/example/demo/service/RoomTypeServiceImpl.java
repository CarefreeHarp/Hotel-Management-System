package com.example.demo.service;

import com.example.demo.entities.RoomType;
import com.example.demo.repository.RoomTypeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los tipos de habitación.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 *
 * El repositorio es ahora un RoomTypeRepository de Spring Data JPA, así que el
 * catálogo se guarda en la base de datos H2. Las validaciones y los mensajes de
 * error siguen viviendo aquí: el controlador solo atrapa la excepción y decide
 * a qué pantalla lleva.
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
                .orElseThrow(() -> new NoSuchElementException("No room type named " + name + " exists."));
    }

    @Override
    public RoomType findById(int roomTypeId) {
        return typeRoomRepository.findById(roomTypeId)
                .orElseThrow(() -> new NoSuchElementException(
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
     * Reglas del negocio: el name no se puede repetir, el price por noche no
     * puede ser negativo y la habitación tiene que recibir al menos a una persona.
     * El name se compara contra el id porque, al editar, el propio tipo conserva
     * su name y eso no debe contar como duplicado.
     *
     * Los ids se comparan con Objects.equals y no con != porque son Integer (un
     * objeto), y en un tipo nuevo el id todavía viene en null.
     *
     * @throws IllegalArgumentException con el mensaje del primer dato inválido.
     */
    private void validateData(RoomType typeRoom) {
        RoomType typeWithThatName = typeRoomRepository.findByNameIgnoreCase(typeRoom.getName()).orElse(null);
        if (typeWithThatName != null
                && !Objects.equals(typeWithThatName.getRoomTypeId(), typeRoom.getRoomTypeId())) {
            throw new IllegalArgumentException(
                    "A room type named " + typeRoom.getName() + " already exists.");
        }

        if (typeRoom.getNightlyPrice() == null || typeRoom.getNightlyPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The nightly price cannot be negative.");
        }

        if (typeRoom.getMaxCapacity() == null || typeRoom.getMaxCapacity() < 1) {
            throw new IllegalArgumentException("Maximum capacity must be at least one guest.");
        }
    }
}
