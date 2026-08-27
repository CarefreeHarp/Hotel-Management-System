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
            return "No existe un tipo de habitación llamado " + nombreActual + ".";
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
            return "No existe un tipo de habitación llamado " + nombre + ".";
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
            return "Ya existe un tipo de habitación llamado " + tipoHabitacion.getNombre() + ".";
        }

        if (tipoHabitacion.getPrecioNoche() < 0) {
            return "El precio por noche no puede ser negativo.";
        }

        if (tipoHabitacion.getCapacidadMaxima() < 1) {
            return "La capacidad máxima debe ser de al menos una persona.";
        }

        return null;
    }
}
