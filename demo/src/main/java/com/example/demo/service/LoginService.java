package com.example.demo.service;

import com.example.demo.entities.Client;

/**
 * CAPA DE SERVICIO: autenticación del portal.
 *
 * Validar unas credenciales es lógica de negocio, no una tarea del controlador:
 * el controlador solo debe recibir el usuario y la contraseña del formulario,
 * atrapar el error si lo hay y decidir a qué pantalla lleva el resultado.
 */
public interface LoginService {

    /** Indica si esas credenciales son las del administrador del hotel. */
    boolean isAdministrator(String user, String password);

    /**
     * Devuelve el cliente registrado con ese email y esa contraseña.
     *
     * @throws SecurityException si las credenciales no corresponden a ninguna cuenta.
     */
    Client authenticateClient(String user, String password);
}
