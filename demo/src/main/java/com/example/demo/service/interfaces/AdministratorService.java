package com.example.demo.service.interfaces;

import com.example.demo.entities.Administrator;
import com.example.demo.errors.InvalidAdministratorDataException;
import com.example.demo.errors.ResourceNotFoundException;
import java.util.List;

/**
 * CAPA DE SERVICIO: lógica de negocio de los administradores.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Expone la creación de cuentas desde el panel, la consulta y la edición.
 *
 * Al administrador se le identifica por su adminId autogenerado, igual que al
 * cliente, porque puede cambiar su propio correo al editarse.
 *
 * MANEJO DE ERRORES: el servicio decide qué es un error y con qué mensaje se le
 * explica al usuario, lanzando una excepción propia del proyecto:
 *
 * - ResourceNotFoundException          -> el administrador buscado no existe.
 * - InvalidAdministratorDataException  -> los datos del formulario no son válidos.
 */
public interface AdministratorService {
    void create(Administrator administrator);

    /** Lista todos los administradores registrados. */
    List<Administrator> listAdministrators();

    /**
     * Devuelve el administrador con ese identificador autogenerado.
     *
     * @throws ResourceNotFoundException si no existe un administrador con ese id.
     */
    Administrator findById(Integer adminId);

    /**
     * Actualiza el nombre y el correo de un administrador registrado.
     * adminId identifica la cuenta que se está editando, incluso si el
     * administrador cambia su correo.
     *
     * @throws ResourceNotFoundException         si no existe un administrador con ese id.
     * @throws InvalidAdministratorDataException si los datos nuevos no son válidos.
     */
    void update(Integer adminId, Administrator administrator);
}
