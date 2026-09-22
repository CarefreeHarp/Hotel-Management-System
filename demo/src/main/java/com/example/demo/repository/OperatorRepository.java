package com.example.demo.repository;

import com.example.demo.entities.Operator;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: acceso a la tabla OPERATOR con Spring Data JPA.
 *
 * La llave primaria es operator_id, de tipo Integer, y la genera la base de
 * datos con IDENTITY.
 *
 * La búsqueda por correo se usa al validar que el email no quede repetido entre
 * dos operarios.
 */
@Repository
public interface OperatorRepository extends JpaRepository<Operator, Integer> {

    java.util.List<Operator> findByAdmin_AdminId(Integer adminId);

    boolean existsByAdmin_AdminId(Integer adminId);

    /* Busca un operario por correo electrónico sin distinguir mayúsculas de minúsculas. */
    Optional<Operator> findByEmailIgnoreCase(String email);
}
