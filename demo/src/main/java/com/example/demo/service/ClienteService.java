package com.example.demo.service;

import com.example.demo.entitys.Cliente;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * CAPA DE SERVICIO: lógica de negocio de los clientes.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Al cliente se le identifica por su correo y no por su id, porque el id es
 * interno de la base de datos y el usuario nunca lo ve. El correo, en cambio,
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
public interface ClienteService {

    /** Lista todos los clientes registrados. */
    List<Cliente> listarClientes();

    /**
     * Devuelve el cliente registrado con ese correo.
     *
     * @throws NoSuchElementException si no hay ninguna cuenta con ese correo.
     */
    Cliente buscarPorCorreo(String correo);

    /**
     * Registra un cliente nuevo, validando que el correo y la cédula no estén
     * usados por otro cliente.
     *
     * @throws IllegalArgumentException si los datos no cumplen las reglas del negocio.
     */
    void registrar(Cliente cliente);

    /**
     * Actualiza los datos personales de un cliente registrado después de validar
     * su contraseña actual. correoActual identifica la cuenta que se está editando,
     * porque el cliente puede estar cambiando justamente su correo.
     *
     * @throws NoSuchElementException   si no existe una cuenta con correoActual.
     * @throws SecurityException        si la contraseña actual no coincide.
     * @throws IllegalArgumentException si los datos nuevos no son válidos.
     */
    void actualizarPerfil(String correoActual, Cliente cliente, String passwordActual);

    /**
     * Elimina la cuenta del cliente con ese correo.
     *
     * @throws NoSuchElementException si no hay ninguna cuenta con ese correo.
     */
    void eliminarCuenta(String correo);
}
