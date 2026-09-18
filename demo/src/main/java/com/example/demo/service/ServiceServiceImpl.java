package com.example.demo.service;

import com.example.demo.entities.Service;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.repository.ServiceRepository;
import com.example.demo.service.interfaces.ServiceService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementación de la lógica de negocio de los servicios.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 *
 * El repositorio es un ServiceRepository de Spring Data JPA, así que los
 * servicios se leen de la base de datos H2.
 *
 * Los servicios son de solo lectura, así que el único error posible es pedir uno
 * que no existe: ResourceNotFoundException.
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
        return serviceRepository.findByServiceIdNotOrderByServiceIdAsc(-1);
    }

    /** Verifica que cada ID corresponda a un servicio activo y conserva el orden elegido. */
    @Override
    public List<Service> getActiveServicesByIds(List<Integer> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            return List.of();
        }
        if (serviceIds.stream().anyMatch(serviceId -> serviceId == null)
                || serviceIds.stream().distinct().count() != serviceIds.size()) {
            throw new IllegalArgumentException("A service can only be selected once.");
        }

        Map<Integer, Service> servicesById = new LinkedHashMap<>();
        serviceRepository.findByServiceIdInAndActiveTrue(serviceIds)
                .forEach(service -> servicesById.put(service.getServiceId(), service));
        if (servicesById.size() != serviceIds.size()) {
            throw new IllegalArgumentException("One or more selected services are unavailable.");
        }

        List<Service> selectedServices = new ArrayList<>();
        serviceIds.forEach(serviceId -> selectedServices.add(servicesById.get(serviceId)));
        return selectedServices;
    }

    @Override
    public Service getServiceByUrlName(String urlName) {
        return serviceRepository.findByUrlNameIgnoreCase(urlName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The service " + urlName + " does not exist."));
    }
}
