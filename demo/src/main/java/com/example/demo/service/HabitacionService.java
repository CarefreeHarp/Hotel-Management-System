package com.example.demo.service;

import com.example.demo.entitys.Habitacion;
import java.util.List;

/** Business operations for the hotel room catalog. */
public interface HabitacionService {

    List<Habitacion> listarHabitaciones();

    Habitacion buscarPorNumero(int numero);

    String crear(Habitacion habitacion);

    String actualizar(int numeroActual, Habitacion habitacion);

    String eliminar(int numero);
}
