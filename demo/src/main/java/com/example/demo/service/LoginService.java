package com.example.demo.service;

import com.example.demo.entitys.Cliente;

/**
 * CAPA DE SERVICIO: autenticación del portal.
 *
 * Validar unas credenciales es lógica de negocio, no una tarea del controlador:
 * el controlador solo debe recibir el usuario y la contraseña del formulario y
 * decidir a qué pantalla lleva el resultado.
 */
public interface LoginService {

    /** Indica si esas credenciales son las del administrador del hotel. */
    boolean esAdministrador(String usuario, String password);

    /**
     * Devuelve el cliente registrado con ese correo y esa contraseña,
     * o null cuando las credenciales no corresponden a ninguna cuenta.
     */
    Cliente autenticarCliente(String usuario, String password);
}
