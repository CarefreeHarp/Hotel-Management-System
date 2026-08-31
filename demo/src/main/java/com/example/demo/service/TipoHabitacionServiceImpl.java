package com.example.demo.service;

import com.example.demo.entitys.TipoHabitacion;
import com.example.demo.repository.TipoHabitacionRepositoryMemoria;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los tipos de habitación.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 */
@Service
public class TipoHabitacionServiceImpl implements TipoHabitacionService {

    @Autowired
    public TipoHabitacionRepositoryMemoria tipoHabitacionRepository;

    @Override
    public List<TipoHabitacion> listarTipos() {
        return tipoHabitacionRepository.listarTodos();
    }

    @Override
    public TipoHabitacion buscarPorNombre(String nombre) {
        return tipoHabitacionRepository.listarTodos()
                .stream()
                .filter(tipoHabitacion -> tipoHabitacion.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String crear(TipoHabitacion tipoHabitacion) {
        // El id lo genera el repositorio: el formulario nunca lo envía.
        tipoHabitacion.setIdTipo(0);

        String error = validarDatos(tipoHabitacion);
        if (error != null) {
            return error;
        }

        tipoHabitacionRepository.guardar(tipoHabitacion);
        return null;
    }

    @Override
    public String actualizar(String nombreActual, TipoHabitacion tipoHabitacion) {
        TipoHabitacion tipoRegistrado = buscarPorNombre(nombreActual);
        if (tipoRegistrado == null) {
            return "No room type named " + nombreActual + " exists.";
        }

        // Se conserva el id que ya tenía el tipo para no crear un registro nuevo.
        tipoHabitacion.setIdTipo(tipoRegistrado.getIdTipo());

        String error = validarDatos(tipoHabitacion);
        if (error != null) {
            return error;
        }

        tipoHabitacionRepository.guardar(tipoHabitacion);
        return null;
    }

    @Override
    public String eliminar(String nombre) {
        TipoHabitacion tipoRegistrado = buscarPorNombre(nombre);
        if (tipoRegistrado == null) {
            return "No room type named " + nombre + " exists.";
        }

        tipoHabitacionRepository.eliminar(tipoRegistrado.getIdTipo());
        return null;
    }

    /**
     * Reglas del negocio: el nombre no se puede repetir, el precio por noche no
     * puede ser negativo y la habitación tiene que recibir al menos a una persona.
     * El nombre se compara contra el id porque, al editar, el propio tipo conserva
     * su nombre y eso no debe contar como duplicado.
     */
    private String validarDatos(TipoHabitacion tipoHabitacion) {
        TipoHabitacion tipoConEseNombre = buscarPorNombre(tipoHabitacion.getNombre());
        if (tipoConEseNombre != null && tipoConEseNombre.getIdTipo() != tipoHabitacion.getIdTipo()) {
            return "A room type named " + tipoHabitacion.getNombre() + " already exists.";
        }

        if (tipoHabitacion.getPrecioNoche() < 0) {
            return "The nightly price cannot be negative.";
        }

        if (tipoHabitacion.getCapacidadMaxima() < 1) {
            return "Maximum capacity must be at least one guest.";
        }

        return null;
    }
}
