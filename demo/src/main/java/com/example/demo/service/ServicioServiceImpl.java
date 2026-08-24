package com.example.demo.service;

import com.example.demo.entitys.Servicio;
import com.example.demo.repository.ServicioRepositoryMemoria;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los servicios.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * por constructor (inyección de dependencias).
 */
@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    public ServicioRepositoryMemoria servicioRepository;

    /**
     * Ordena los servicios por categoría y, dentro de cada categoría, por nombre,
     * para que la tabla de la vista quede agrupada y sea fácil de leer.
     */
    @Override
    public List<Servicio> listarServicios() {
        return servicioRepository.listarTodos()
                .stream()
                .sorted(Comparator.comparing(Servicio::getCategoria)
                        .thenComparing(Servicio::getNombre))
                .toList();
    }

    @Override
    public Servicio getServiceByNombreUrl(String nombreUrl) {
        return servicioRepository.obtenerPorNombreUrl(nombreUrl);
    }
}
