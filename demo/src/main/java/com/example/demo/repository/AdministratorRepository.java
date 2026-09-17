package com.example.demo.repository;

import com.example.demo.entities.Administrator;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: acceso a la tabla ADMINISTRATOR con Spring Data JPA.
 *
 * Al extender JpaRepository ya se heredan findAll, findById, save, deleteById,
 * existsById y count sin escribir código. El segundo tipo genérico es el de la
 * llave primaria (admin_id, de tipo Integer).
 *
 * El método de abajo es una "derived query": Spring Data lee el nombre del
 * método y genera la consulta, así que no hay que escribir SQL. Devuelve
 * Optional porque puede que no exista ningún administrador con ese correo, y es
 * la capa de servicio la que decide si eso es un error.
 */
@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    /* Busca un administrador por correo electrónico sin distinguir mayúsculas de minúsculas. */
    Optional<Administrator> findByEmailIgnoreCase(String email);
}
