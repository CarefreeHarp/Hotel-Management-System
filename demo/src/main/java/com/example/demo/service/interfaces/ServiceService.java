package com.example.demo.service.interfaces;

import com.example.demo.entities.Service;
import com.example.demo.errors.ResourceNotFoundException;

import java.util.List;

/**
 * CAPA DE SERVICIO: define la lógica de negocio de los servicios del hotel.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 */
public interface ServiceService {

    /** Devuelve los servicios que se muestran en la carta pública del hotel. */
    List<Service> listServices();

    /** Obtiene servicios activos por ID y rechaza IDs inexistentes, inactivos o repetidos. */
    List<Service> getActiveServicesByIds(List<Integer> serviceIds);

    /**
     * Devuelve un servicio por su urlName.
     *
     * @throws ResourceNotFoundException si no existe un servicio con ese urlName.
     */
    Service getServiceByUrlName(String urlName);
}
