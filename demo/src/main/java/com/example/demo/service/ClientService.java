package com.example.demo.service;

import com.example.demo.entities.Client;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * CAPA DE SERVICIO: lógica de negocio de los clientes.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Al cliente se le identifica por su email y no por su id, porque el id es
 * interno de la base de datos y el usuario nunca lo ve. El email, en cambio,
 * es único y es el dato con el que el cliente se registra e inicia sesión.
 *
 * MANEJO DE ERRORES: el servicio es el que decide qué es un error y con qué
 * mensaje se le explica al usuario. Cuando una regla del negocio no se cumple
 * lanza una excepción genérica de Java con un mensaje personalizado, y el
 * controlador solo la atrapa para decidir a qué pantalla lleva cada caso:
 *
 * - NoSuchElementException  -> la cuenta buscada no existe.
 * - IllegalArgumentException -> los datos enviados no son válidos.
 * - SecurityException        -> la contraseña de confirmación no coincide.
 */
public interface ClientService {

    /** Lista todos los clientes registrados. */
    List<Client> listClients();

    /**
     * Devuelve el cliente registrado con ese email.
     *
     * @throws NoSuchElementException si no hay ninguna cuenta con ese email.
     */
    Client findByEmail(String email);

    /**
     * Registra un cliente nuevo, validando que el email y la cédula no estén
     * usados por otro cliente.
     *
     * @throws IllegalArgumentException si los datos no cumplen las reglas del negocio.
     */
    void register(Client client);

    /**
     * Actualiza los datos personales de un cliente registrado después de validar
     * su contraseña actual. correoActual identifica la cuenta que se está editando,
     * porque el cliente puede estar cambiando justamente su email.
     *
     * @throws NoSuchElementException   si no existe una cuenta con correoActual.
     * @throws SecurityException        si la contraseña actual no coincide.
     * @throws IllegalArgumentException si los datos nuevos no son válidos.
     */
    void updateProfile(String emailCurrent, Client client, String passwordCurrent);

    /**
     * Elimina la cuenta del cliente con ese email.
     *
     * @throws NoSuchElementException si no hay ninguna cuenta con ese email.
     */
    void deleteProfile(String email);
}
