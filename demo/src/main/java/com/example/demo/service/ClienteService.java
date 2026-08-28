package com.example.demo.service;

import com.example.demo.entitys.Cliente;
import java.util.List;

/**
 * CAPA DE SERVICIO: lógica de negocio de los clientes.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 *
 * Al cliente se le identifica por su correo y no por su id, porque el id es
 * interno de la base de datos y el usuario nunca lo ve. El correo, en cambio,
 * es único y es el dato con el que el cliente se registra e inicia sesión.
 *
 * Los métodos que modifican datos devuelven un String: null cuando la operación
 * salió bien, o el mensaje de error que se le debe mostrar al usuario cuando no.
 */
public interface ClienteService {

    /** Lista todos los clientes registrados. */
    List<Cliente> listarClientes();

    /** Devuelve el cliente con ese correo, o null si no hay ninguno registrado. */
    Cliente buscarPorCorreo(String correo);

    /**
     * Registra un cliente nuevo, validando que el correo y la cédula no estén
     * usados por otro cliente.
     */
    String registrar(Cliente cliente);

    /**
     * Updates a registered client's personal data after validating its current password.
     * correoActual identifica la cuenta que se está editando, porque el cliente
     * puede estar cambiando justamente su correo.
     */
    String actualizarPerfil(String correoActual, Cliente cliente, String passwordActual);

    /** Elimina la cuenta del cliente con ese correo. */
    String eliminarCuenta(String correo);
}
