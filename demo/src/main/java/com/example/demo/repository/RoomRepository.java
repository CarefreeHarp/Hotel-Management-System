package com.example.demo.repository;

import com.example.demo.entities.Room;
import com.example.demo.entities.enums.FolioStatus;
import com.example.demo.entities.enums.ReservationStatus;
import com.example.demo.entities.enums.RoomStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /* Pagina habitaciones sin reservas vigentes que se crucen con el intervalo solicitado. */
    @EntityGraph(attributePaths = "roomType")
    /* Busca habitaciones disponibles sin cargar sus imágenes secundarias de forma anticipada. */
    @Query("""
            SELECT room FROM Room room
            WHERE room.status <> :maintenanceStatus
              AND NOT EXISTS (
                SELECT reservation FROM Reservation reservation
                WHERE reservation.room = room
                  AND reservation.status <> :cancelledStatus
                  AND reservation.checkInDate < :checkOutDate
                  AND reservation.checkOutDate > :checkInDate
            )
            ORDER BY room.number ASC
            """)
    Page<Room> findAvailableForStay(@Param("checkInDate") LocalDate checkInDate,
                                    @Param("checkOutDate") LocalDate checkOutDate,
                                    @Param("cancelledStatus") ReservationStatus cancelledStatus,
                                    @Param("maintenanceStatus") RoomStatus maintenanceStatus,
                                    Pageable pageable);

    @Modifying
    /* Marca como disponibles las habitaciones que no están en mantenimiento. */
    @Query("UPDATE Room room SET room.status = :availableStatus WHERE room.status <> :maintenanceStatus")
    int markRoomsAsAvailable(@Param("availableStatus") RoomStatus availableStatus,
                             @Param("maintenanceStatus") RoomStatus maintenanceStatus);

    @Modifying
    /* Marca como ocupadas las habitaciones con un folio pagado durante una estadía vigente. */
    @Query("""
            UPDATE Room room
            SET room.status = :occupiedStatus
            WHERE room.status <> :maintenanceStatus
              AND EXISTS (
                SELECT folio FROM Folio folio
                WHERE folio.reservation.room = room
                  AND folio.status = :paidStatus
                  AND folio.reservation.status <> :cancelledReservationStatus
                  AND folio.reservation.checkInDate < :currentDate
                  AND folio.reservation.checkOutDate > :currentDate
              )
            """)
    int markPaidStayRoomsAsOccupied(@Param("occupiedStatus") RoomStatus occupiedStatus,
                                    @Param("maintenanceStatus") RoomStatus maintenanceStatus,
                                    @Param("paidStatus") FolioStatus paidStatus,
                                    @Param("cancelledReservationStatus") ReservationStatus cancelledReservationStatus,
                                    @Param("currentDate") LocalDate currentDate);
}
