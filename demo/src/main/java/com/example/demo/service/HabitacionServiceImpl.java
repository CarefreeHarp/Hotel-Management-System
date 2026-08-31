package com.example.demo.service;

import com.example.demo.entitys.Habitacion;
import com.example.demo.repository.HabitacionRepositoryMemoria;
import com.example.demo.repository.TipoHabitacionRepositoryMemoria;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Validates room data before persisting it in the in-memory repository. */
@Service
public class HabitacionServiceImpl implements HabitacionService {

    @Autowired
    HabitacionRepositoryMemoria habitacionRepository;

    @Autowired
    TipoHabitacionRepositoryMemoria tipoHabitacionRepository;

    @Override
    public List<Habitacion> listarHabitaciones() {
        return habitacionRepository.listarTodos();
    }

    @Override
    public Habitacion buscarPorNumero(int numero) {
        return habitacionRepository.buscarPorNumero(numero);
    }

    @Override
    public String crear(Habitacion habitacion) {
        String error = validarDatos(habitacion, 0);
        if (error != null) {
            return error;
        }

        habitacionRepository.guardar(habitacion);
        return null;
    }

    @Override
    public String actualizar(int numeroActual, Habitacion habitacion) {
        Habitacion habitacionActual = buscarPorNumero(numeroActual);
        if (habitacionActual == null) {
            return "The room " + numeroActual + " does not exist.";
        }

        String error = validarDatos(habitacion, numeroActual);
        if (error != null) {
            return error;
        }

        if (numeroActual != habitacion.getNumero()) {
            habitacionRepository.eliminar(numeroActual);
        }
        habitacionRepository.guardar(habitacion);
        return null;
    }

    @Override
    public String eliminar(int numero) {
        if (!habitacionRepository.eliminar(numero)) {
            return "The room " + numero + " does not exist.";
        }
        return null;
    }

    private String validarDatos(Habitacion habitacion, int numeroActual) {
        normalizarFotos(habitacion);

        if (habitacion.getNumero() < 1) {
            return "The room number must be greater than zero.";
        }

        Habitacion conEseNumero = buscarPorNumero(habitacion.getNumero());
        if (conEseNumero != null && habitacion.getNumero() != numeroActual) {
            return "A room with that number already exists.";
        }

        if (habitacion.getPiso() < 0) {
            return "The floor cannot be negative.";
        }

        if (habitacion.getEstado() == null) {
            return "Select a room status.";
        }

        if (tipoHabitacionRepository.buscarPorId(habitacion.getIdTipo()) == null) {
            return "Select an existing room type.";
        }

        if (!esUrlHttpValida(habitacion.getFotoPrincipal())) {
            return "The main room photo must be a valid HTTP or HTTPS URL.";
        }

        for (String foto : habitacion.getFotos()) {
            if (!esUrlHttpValida(foto)) {
                return "Each additional room photo must be a valid HTTP or HTTPS URL.";
            }
        }

        return null;
    }

    private void normalizarFotos(Habitacion habitacion) {
        if (habitacion.getFotoPrincipal() != null) {
            habitacion.setFotoPrincipal(habitacion.getFotoPrincipal().trim());
        }

        List<String> fotos = habitacion.getFotos() == null ? new ArrayList<>() : habitacion.getFotos();
        habitacion.setFotos(new ArrayList<>(fotos.stream()
                .filter(foto -> foto != null && !foto.isBlank())
                .map(String::trim)
                .toList()));
    }

    private boolean esUrlHttpValida(String url) {
        if (url == null || url.isBlank()) {
            return true;
        }

        try {
            URI uri = URI.create(url);
            return uri.isAbsolute() && ("http".equalsIgnoreCase(uri.getScheme())
                    || "https".equalsIgnoreCase(uri.getScheme()));
        } catch (IllegalArgumentException error) {
            return false;
        }
    }
}
