package com.example.demo.service;

import com.example.demo.entitys.TipoHabitacion;
import com.example.demo.repository.TipoHabitacionRepositoryMemoria;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la lógica de negocio de los tipos de habitación.
 * Spring la registra como bean gracias a @Service y le inyecta el repositorio
 * con @Autowired (inyección de dependencias).
 *
 * Las validaciones y los mensajes de error viven aquí: el controlador solo
 * atrapa la excepción y decide a qué pantalla lleva.
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
        TipoHabitacion tipoHabitacion = buscarTipo(nombre);
        if (tipoHabitacion == null) {
            throw new NoSuchElementException("No room type named " + nombre + " exists.");
        }

        return tipoHabitacion;
    }

    @Override
    public TipoHabitacion buscarPorId(int idTipo) {
        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.buscarPorId(idTipo);
        if (tipoHabitacion == null) {
            throw new NoSuchElementException("No room type with the id " + idTipo + " exists.");
        }

        return tipoHabitacion;
    }

    @Override
    public void crear(TipoHabitacion tipoHabitacion) {
        // El id lo genera el repositorio: el formulario nunca lo envía.
        tipoHabitacion.setIdTipo(0);

        validarDatos(tipoHabitacion);
        tipoHabitacionRepository.guardar(tipoHabitacion);
    }

    @Override
    public void actualizar(String nombreActual, TipoHabitacion tipoHabitacion) {
        TipoHabitacion tipoRegistrado = buscarPorNombre(nombreActual);

        // Se conserva el id que ya tenía el tipo para no crear un registro nuevo.
        tipoHabitacion.setIdTipo(tipoRegistrado.getIdTipo());

        validarDatos(tipoHabitacion);
        tipoHabitacionRepository.guardar(tipoHabitacion);
    }

    @Override
    public void eliminar(String nombre) {
        TipoHabitacion tipoRegistrado = buscarPorNombre(nombre);
        tipoHabitacionRepository.eliminar(tipoRegistrado.getIdTipo());
    }

    /**
     * Búsqueda interna que sí puede devolver null, porque las validaciones
     * necesitan preguntar si un nombre ya está usado sin que eso sea un error.
     */
    private TipoHabitacion buscarTipo(String nombre) {
        return tipoHabitacionRepository.listarTodos()
                .stream()
                .filter(tipoHabitacion -> tipoHabitacion.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    /**
     * Reglas del negocio: el nombre no se puede repetir, el precio por noche no
     * puede ser negativo y la habitación tiene que recibir al menos a una persona.
     * El nombre se compara contra el id porque, al editar, el propio tipo conserva
     * su nombre y eso no debe contar como duplicado.
     *
     * @throws IllegalArgumentException con el mensaje del primer dato inválido.
     */
    private void validarDatos(TipoHabitacion tipoHabitacion) {
        TipoHabitacion tipoConEseNombre = buscarTipo(tipoHabitacion.getNombre());
        if (tipoConEseNombre != null && tipoConEseNombre.getIdTipo() != tipoHabitacion.getIdTipo()) {
            throw new IllegalArgumentException(
                    "A room type named " + tipoHabitacion.getNombre() + " already exists.");
        }

        if (tipoHabitacion.getPrecioNoche() < 0) {
            throw new IllegalArgumentException("The nightly price cannot be negative.");
        }

        if (tipoHabitacion.getCapacidadMaxima() < 1) {
            throw new IllegalArgumentException("Maximum capacity must be at least one guest.");
        }
    }
}
