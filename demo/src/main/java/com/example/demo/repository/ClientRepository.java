package com.example.demo.repository;

import com.example.demo.entities.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: acceso a la tabla CLIENT con Spring Data JPA.
 *
 * Al extender JpaRepository ya se heredan las operaciones básicas sin escribir
 * código: findAll, findById, save, deleteById, existsById y count. El segundo
 * tipo genérico es el de la llave primaria (client_id, de tipo Integer).
 *
 * Los dos métodos declarados abajo son "derived queries": Spring Data lee el
 * nombre del método y genera él mismo la consulta, así que tampoco hay que
 * escribir SQL. Antes estas búsquedas se hacían recorriendo la lista completa
 * de clientes en la capa de servicio; ahora las resuelve la base de datos.
 *
 * Devuelven Optional porque puede que no exista ningún cliente con ese dato, y
 * es la capa de servicio la que decide si eso es un error.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    /** SELECT * FROM client WHERE UPPER(email) = UPPER(?) */
    Optional<Client> findByEmailIgnoreCase(String email);

    /** SELECT * FROM client WHERE national_id = ? */
    Optional<Client> findByNationalId(String nationalId);
}
