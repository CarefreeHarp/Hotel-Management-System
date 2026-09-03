package com.example.demo.service;

import com.example.demo.entitys.Habitacion;
import com.example.demo.repository.HabitacionRepositoryMemoria;
import com.example.demo.repository.TipoHabitacionRepositoryMemoria;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Valida los datos de la habitación antes de guardarla en el repositorio en memoria.
 * Cuando un dato no cumple una regla del negocio se lanza una excepción con el
 * mensaje que verá el administrador.
 */
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
        Habitacion habitacion = habitacionRepository.buscarPorNumero(numero);
        if (habitacion == null) {
            throw new NoSuchElementException("The room " + numero + " does not exist.");
        }

        return habitacion;
    }

    @Override
    public void crear(Habitacion habitacion) {
        validarDatos(habitacion, 0);
        habitacionRepository.guardar(habitacion);
    }

    @Override
    public void actualizar(int numeroActual, Habitacion habitacion) {
        // Solo se comprueba que exista: si no, buscarPorNumero lanza la excepción.
        buscarPorNumero(numeroActual);

        validarDatos(habitacion, numeroActual);

        if (numeroActual != habitacion.getNumero()) {
            habitacionRepository.eliminar(numeroActual);
        }
        habitacionRepository.guardar(habitacion);
    }

    @Override
    public void eliminar(int numero) {
        if (!habitacionRepository.eliminar(numero)) {
            throw new NoSuchElementException("The room " + numero + " does not exist.");
        }
    }

    /**
     * Reglas del negocio de la habitación.
     * Se consulta el repositorio directamente y no buscarPorNumero porque aquí
     * preguntar si el número ya está usado no es un error, es parte de la validación.
     *
     * @throws IllegalArgumentException con el mensaje del primer dato inválido.
     */
    private void validarDatos(Habitacion habitacion, int numeroActual) {
        normalizarFotos(habitacion);

        if (habitacion.getNumero() < 1) {
            throw new IllegalArgumentException("The room number must be greater than zero.");
        }

        Habitacion conEseNumero = habitacionRepository.buscarPorNumero(habitacion.getNumero());
        if (conEseNumero != null && habitacion.getNumero() != numeroActual) {
            throw new IllegalArgumentException("A room with that number already exists.");
        }

        if (habitacion.getPiso() < 0) {
            throw new IllegalArgumentException("The floor cannot be negative.");
        }

        if (habitacion.getEstado() == null) {
            throw new IllegalArgumentException("Select a room status.");
        }

        if (tipoHabitacionRepository.buscarPorId(habitacion.getIdTipo()) == null) {
            throw new IllegalArgumentException("Select an existing room type.");
        }

        if (!esUrlHttpValida(habitacion.getFotoPrincipal())) {
            throw new IllegalArgumentException("The main room photo must be a valid HTTP or HTTPS URL.");
        }

        for (String foto : habitacion.getFotos()) {
            if (!esUrlHttpValida(foto)) {
                throw new IllegalArgumentException("Each additional room photo must be a valid HTTP or HTTPS URL.");
            }
        }
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
