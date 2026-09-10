package com.example.demo.repository;

import com.example.demo.entities.Service;
import java.util.List;
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

    /* Busca un servicio por su nombre de URL sin distinguir mayúsculas de minúsculas. */
    Optional<Service> findByUrlNameIgnoreCase(String urlName);

    /* Lista los servicios excepto el indicado, ordenados ascendentemente por identificador. */
    List<Service> findByServiceIdNotOrderByServiceIdAsc(Integer serviceId);
}
