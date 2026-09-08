package com.example.demo.errors;

/**
 * Se lanza cuando se pide un tipo de habitación que no está en el catálogo.
 *
 * Tiene dos constructores porque al tipo se le busca de dos formas: por su name,
 * que es como lo identifican las pantallas del administrador, y por su id, que es
 * lo que manda el desplegable del formulario de habitaciones.
 */
public class RoomTypeNotFoundException extends RuntimeException {

    public RoomTypeNotFoundException(String name) {
        super("No room type named " + name + " exists.");
    }

    public RoomTypeNotFoundException(int roomTypeId) {
        super("No room type with the id " + roomTypeId + " exists.");
    }
}
