package com.example.demo.repository;

import com.example.demo.entities.Service;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: acceso a la tabla SERVICE con Spring Data JPA.
 *
 * Se importa com.example.demo.entities.Service (la entidad) y no
 * org.springframework.stereotype.Service (la anotación), que se llama igual.
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

    /**
     * SELECT * FROM service WHERE UPPER(url_name) = UPPER(?)
     *
     * El urlName es el identificador que viaja en la URL pública del servicio
     * (por ejemplo /servicios/spa) y es único en la tabla.
     */
    Optional<Service> findByUrlNameIgnoreCase(String urlName);
}
