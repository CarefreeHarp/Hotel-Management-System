package com.example.demo.repository;

import com.example.demo.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: acceso a la tabla ROOM con Spring Data JPA.
 *
 * Aquí no hace falta declarar ningún método propio: la llave primaria es el
 * número de la habitación (room_number), así que buscarla por número ya es el
 * findById que se hereda de JpaRepository.
 *
 * A diferencia de las demás entidades, este id NO lo genera la base de datos:
 * el número lo asigna el hotel porque identifica la habitación física.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
