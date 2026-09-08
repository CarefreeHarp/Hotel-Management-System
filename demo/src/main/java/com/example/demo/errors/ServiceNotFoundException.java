package com.example.demo.errors;

/**
 * Se lanza cuando se pide un servicio que no existe.
 *
 * Al servicio se le identifica por su urlName, que es el nombre apto para URL
 * que viaja en la dirección pública de la pantalla (por ejemplo /services/spa),
 * así que es ese valor el que se le muestra al usuario en el mensaje.
 *
 * Los servicios son de solo lectura en el portal: no hay pantalla de creación ni
 * de edición, por eso el servicio no necesita una excepción de datos inválidos
 * como sí la tienen los clientes, las habitaciones y los tipos de habitación.
 */
public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException(String urlName) {
        super("The service " + urlName + " does not exist.");
    }
}
