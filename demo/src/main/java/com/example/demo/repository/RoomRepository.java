package com.example.demo.repository;

import com.example.demo.entities.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: acceso a la tabla ROOM con Spring Data JPA.
 *
 * La llave primaria es room_id. Como el número físico de habitación es un dato
 * de negocio único, se declara una consulta derivada para buscarlo.
 *
 * El número lo asigna el hotel porque identifica la habitación física; room_id
 * es el identificador técnico generado por la base de datos.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    Optional<Room> findByNumber(Integer number);

    boolean existsByNumber(Integer number);
}
