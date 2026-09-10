package com.example.demo.service;

import com.example.demo.entities.Client;
import com.example.demo.errors.InvalidClientDataException;
import com.example.demo.errors.InvalidCurrentPasswordException;
import com.example.demo.errors.ResourceNotFoundException;
import java.util.List;

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
 * lanza una excepción personalizada con un mensaje, y el
 * controlador solo la atrapa para decidir a qué pantalla lleva cada caso:
 *
 * - ResourceNotFoundException -> la cuenta buscada no existe.
 * - InvalidClientDataException -> los datos enviados no son válidos.
 * - InvalidCurrentPasswordException -> la contraseña de confirmación no coincide.
 */
public interface ClientService {

    /** Lista todos los clientes registrados. */
    List<Client> listClients();

    /**
     * Devuelve el cliente registrado con ese identificador autogenerado.
     *
     * @throws ResourceNotFoundException si no hay ninguna cuenta con ese identificador.
     */
    Client findById(Integer clientId);

    /**
     * Devuelve el cliente con ese email para autenticarlo. Si no existe, lanza
     * NoSuchElementException para que el inicio de sesión responda con el mismo
     * mensaje genérico que usa para una contraseña incorrecta.
     */
    Client findByEmailForLogin(String email);

    /**
     * Registra un cliente nuevo, validando que el email y la cédula no estén
     * usados por otro cliente.
     *
     * @throws InvalidClientDataException si los datos no cumplen las reglas del negocio.
     */
    void register(Client client);

    /**
     * Actualiza los datos personales de un cliente registrado después de validar
     * su contraseña actual. clientId identifica la cuenta que se está editando,
     * incluso si el cliente cambia su email.
     *
     * @throws ResourceNotFoundException si no existe una cuenta con clientId.
     * @throws InvalidCurrentPasswordException si la contraseña actual no coincide.
     * @throws InvalidClientDataException si los datos nuevos no son válidos.
     */
    void updateProfile(Integer clientId, Client client, String passwordCurrent);

    /**
     * Elimina la cuenta del cliente con ese identificador.
     *
     * @throws ResourceNotFoundException si no hay ninguna cuenta con ese identificador.
     */
    void deleteProfile(Integer clientId);
}
