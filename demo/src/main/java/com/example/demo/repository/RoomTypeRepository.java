package com.example.demo.repository;

import com.example.demo.entities.RoomType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: acceso a la tabla ROOM_TYPE con Spring Data JPA.
 *
 * La llave primaria es room_type_id, de tipo Integer, y la genera la base de
 * datos con IDENTITY: por eso un tipo nuevo se guarda con el id en null.
 */
@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    /**
     * SELECT * FROM room_type WHERE UPPER(name) = UPPER(?)
     *
     * El name es único en la tabla, así que como máximo devuelve un registro.
     */
    Optional<RoomType> findByNameIgnoreCase(String name);
}
