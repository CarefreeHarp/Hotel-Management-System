package com.example.demo.service;

import com.example.demo.entitys.Servicio;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * CAPA DE SERVICIO: define la lógica de negocio de los servicios del hotel.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 */
public interface ServicioService {

    /** Devuelve los servicios que se muestran en la carta pública del hotel. */
    List<Servicio> listarServicios();

    /**
     * Devuelve un servicio por su nombreUrl.
     *
     * @throws NoSuchElementException si no existe un servicio con ese nombreUrl.
     */
    Servicio getServiceByNombreUrl(String nombreUrl);
}
