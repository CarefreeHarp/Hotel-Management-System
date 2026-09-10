package com.example.demo.repository;

import com.example.demo.entities.Room;
import java.util.List;
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

    /* Busca una habitación por su número físico. */
    Optional<Room> findByNumber(Integer number);

    /* Verifica si existe una habitación con el número físico indicado. */
    boolean existsByNumber(Integer number);

    /* Lista las habitaciones excepto la indicada, ordenadas ascendentemente por número. */
    List<Room> findByRoomIdNotOrderByNumberAsc(Integer roomId);
}
