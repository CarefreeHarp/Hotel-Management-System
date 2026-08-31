package com.example.demo.repository;

import com.example.demo.entitys.Habitacion;
import com.example.demo.entitys.EstadoHabitacion;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * In-memory repository for hotel rooms. The room number is the primary key
 * because it identifies the physical room in the hotel.
 */
@Repository
public class HabitacionRepositoryMemoria {

    private final Map<Integer, Habitacion> habitaciones = new LinkedHashMap<>();

    public HabitacionRepositoryMemoria() {
        cargarDatosDePrueba();
    }

    /** Loads rooms that reference the default room types in the in-memory catalog. */
    private void cargarDatosDePrueba() {
        guardar(new Habitacion(101, 1, EstadoHabitacion.DISPONIBLE, true, 1,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304",
                List.of("https://images.unsplash.com/photo-1618773928121-c32242e63f39")));
        guardar(new Habitacion(205, 2, EstadoHabitacion.OCUPADA, false, 2,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427",
                List.of()));
        guardar(new Habitacion(307, 3, EstadoHabitacion.MANTENIMIENTO, false, 3,
                "https://images.unsplash.com/photo-1584132967334-10e028bd69f7",
                List.of()));
        guardar(new Habitacion(401, 4, EstadoHabitacion.DISPONIBLE, true, 4,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea",
                List.of("https://images.unsplash.com/photo-1600607687939-ce8a6c25118c")));
    }

    public List<Habitacion> listarTodos() {
        return new ArrayList<>(habitaciones.values());
    }

    public Habitacion buscarPorNumero(int numero) {
        return habitaciones.get(numero);
    }

    public Habitacion guardar(Habitacion habitacion) {
        habitaciones.put(habitacion.getNumero(), habitacion);
        return habitacion;
    }

    public boolean eliminar(int numero) {
        return habitaciones.remove(numero) != null;
    }
}
