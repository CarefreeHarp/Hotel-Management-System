package com.example.demo.service;

import com.example.demo.entities.Service;
import com.example.demo.repository.ServiceInMemoryRepository;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementación de la lógica de negocio de los servicios.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * por constructor (inyección de dependencias).
 */
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    public ServiceInMemoryRepository serviceRepository;

    /**
     * Ordena los servicios por categoría y, dentro de cada categoría, por name,
     * para que la tabla de la vista quede agrupada y sea fácil de leer.
     */
    @Override
    public List<Service> listServices() {
        return serviceRepository.listAll()
                .stream()
                .sorted(Comparator.comparingInt(Service::getServiceId))
                .toList();
    }

    @Override
    public Service getServiceByUrlName(String urlName) {
        Service service = serviceRepository.findByUrlName(urlName);
        if (service == null) {
            throw new NoSuchElementException("The service " + urlName + " does not exist.");
        }

        return service;
    }
}
