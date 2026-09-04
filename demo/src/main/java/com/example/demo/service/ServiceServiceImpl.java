package com.example.demo.service;

import com.example.demo.entities.Service;
import com.example.demo.repository.ServiceRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

/**
 * Implementación de la lógica de negocio de los servicios.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 *
 * El repositorio es ahora un ServiceRepository de Spring Data JPA, así que los
 * servicios se leen de la base de datos H2.
 */
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    public ServiceRepository serviceRepository;

    /**
     * Antes la lista se ordenaba en Java después de traerla completa; ahora el
     * orden se lo pide a la base de datos con un Sort, que Spring Data traduce
     * a un ORDER BY service_id.
     */
    @Override
    public List<Service> listServices() {
        return serviceRepository.findAll(Sort.by("serviceId"));
    }

    @Override
    public Service getServiceByUrlName(String urlName) {
        return serviceRepository.findByUrlNameIgnoreCase(urlName)
                .orElseThrow(() -> new NoSuchElementException(
                        "The service " + urlName + " does not exist."));
    }
}
