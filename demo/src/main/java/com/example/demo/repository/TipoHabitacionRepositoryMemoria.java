package com.example.demo.repository;

import com.example.demo.entitys.TipoHabitacion;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: simula la tabla TIPO_HABITACION de la base de datos con
 * un mapa en memoria, donde la llave es el id del tipo (la PK).
 *
 * Aquí solo se guarda y se consulta. Las búsquedas por otros campos y las
 * validaciones del negocio viven en la capa de servicio.
 */
@Repository
public class TipoHabitacionRepositoryMemoria {

    private final Map<Integer, TipoHabitacion> tiposHabitacion = new LinkedHashMap<>();

    /**
     * Último id entregado. Simula el AUTO_INCREMENT de la base de datos:
     * siempre crece, nunca reutiliza ids de registros eliminados.
     */
    private int ultimoId = 0;

    public TipoHabitacionRepositoryMemoria() {
        cargarDatosDePrueba();
    }

    /** Los cuatro tipos de habitación que ofrece el hotel. */
    private void cargarDatosDePrueba() {
        guardar(new TipoHabitacion(0, "Normal",
                "Habitación cómoda con lo esencial para una estadía tranquila: cama doble, escritorio y baño privado.",
                250000, 2));
        guardar(new TipoHabitacion(0, "Executive",
                "Habitación amplia con zona de trabajo, cafetera y vista a la ciudad, pensada para viajes de negocios.",
                380000, 3));
        guardar(new TipoHabitacion(0, "VIP",
                "Suite con sala independiente, minibar surtido y acceso preferencial a las zonas de bienestar.",
                520000, 4));
        guardar(new TipoHabitacion(0, "Luxury",
                "Suite de lujo con terraza privada, jacuzzi y atención personalizada durante toda la estadía.",
                750000, 6));
    }

    public List<TipoHabitacion> listarTodos() {
        return new ArrayList<>(tiposHabitacion.values());
    }

    /** Devuelve el tipo con ese id, o null si no existe. */
    public TipoHabitacion buscarPorId(int idTipo) {
        return tiposHabitacion.get(idTipo);
    }

    /**
     * Guarda el tipo de habitación. Si llega sin id (en 0) es un registro nuevo y
     * el id se genera automáticamente; si ya trae id, se sobrescribe el existente.
     */
    public TipoHabitacion guardar(TipoHabitacion tipoHabitacion) {
        if (tipoHabitacion.getIdTipo() == 0) {
            ultimoId++;
            tipoHabitacion.setIdTipo(ultimoId);
        }

        tiposHabitacion.put(tipoHabitacion.getIdTipo(), tipoHabitacion);
        return tipoHabitacion;
    }

    /** Devuelve true si el tipo existía y se eliminó. */
    public boolean eliminar(int idTipo) {
        return tiposHabitacion.remove(idTipo) != null;
    }
}
