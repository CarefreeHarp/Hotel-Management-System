package com.example.demo.service;

import com.example.demo.entities.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * CAPA DE SERVICIO: define la lógica de negocio de los servicios del hotel.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 */
public interface ServiceService {

    /** Devuelve los servicios que se muestran en la carta pública del hotel. */
    List<Service> listServices();

    /**
     * Devuelve un servicio por su urlName.
     *
     * @throws NoSuchElementException si no existe un servicio con ese urlName.
     */
    Service getServiceByUrlName(String urlName);
}
