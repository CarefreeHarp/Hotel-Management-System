package com.example.demo.service;

import com.example.demo.entitys.Servicio;
import java.util.List;

/**
 * CAPA DE SERVICIO: define la lógica de negocio de los servicios del hotel.
 * El controlador solo conoce esta interfaz, nunca el repositorio.
 */
public interface ServicioService {

    /** Devuelve los servicios que se muestran en la carta pública del hotel. */
    List<Servicio> listarServicios();
}
