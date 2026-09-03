package com.example.demo.service;

import com.example.demo.entitys.Habitacion;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * CAPA DE SERVICIO: lógica de negocio del catálogo de habitaciones.
 *
 * MANEJO DE ERRORES: las reglas del negocio se validan aquí y el error se
 * comunica lanzando una excepción genérica con un mensaje personalizado:
 *
 * - NoSuchElementException   -> la habitación buscada no existe.
 * - IllegalArgumentException -> los datos del formulario no son válidos.
 */
public interface HabitacionService {

    List<Habitacion> listarHabitaciones();

    /**
     * Devuelve la habitación con ese número.
     *
     * @throws NoSuchElementException si no existe una habitación con ese número.
     */
    Habitacion buscarPorNumero(int numero);

    /**
     * Registra una habitación nueva.
     *
     * @throws IllegalArgumentException si los datos no cumplen las reglas del negocio.
     */
    void crear(Habitacion habitacion);

    /**
     * Actualiza una habitación existente. numeroActual es el número que tenía
     * antes de editarla, porque el administrador puede estar cambiándolo.
     *
     * @throws NoSuchElementException   si no existe la habitación numeroActual.
     * @throws IllegalArgumentException si los datos nuevos no son válidos.
     */
    void actualizar(int numeroActual, Habitacion habitacion);

    /**
     * Elimina la habitación con ese número.
     *
     * @throws NoSuchElementException si no existe una habitación con ese número.
     */
    void eliminar(int numero);
}
