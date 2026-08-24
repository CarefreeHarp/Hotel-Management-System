package com.example.demo.repository;

import com.example.demo.entitys.Servicio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * CAPA DE REPOSITORIO: datos de prueba de los servicios en memoria.
 * Se reemplazará por la base de datos real más adelante.
 */
@Repository
public class ServicioRepositoryMemoria {

    private final List<Servicio> servicios = new ArrayList<>();

    public ServicioRepositoryMemoria() {
        servicios.add(new Servicio(1, "Wi-Fi", "Acceso a internet inalámbrico de alta velocidad.", 0, "Conectividad", true));
        servicios.add(new Servicio(2, "Desayuno", "Desayuno buffet servido en el restaurante.", 45000, "Alimentación", true));
        servicios.add(new Servicio(3, "Almuerzo", "Menú del día con bebida incluida.", 55000, "Alimentación", true));
        servicios.add(new Servicio(4, "Cena", "Cena a la carta en el restaurante del hotel.", 65000, "Alimentación", true));
        servicios.add(new Servicio(5, "Servicio a la habitación", "Entrega de alimentos y bebidas en la habitación.", 15000, "Alimentación", true));
        servicios.add(new Servicio(6, "Lavandería", "Lavado y planchado de prendas personales.", 30000, "Lavandería", true));
        servicios.add(new Servicio(7, "Estacionamiento", "Espacio vigilado para el vehículo del huésped.", 25000, "Transporte", true));
        servicios.add(new Servicio(8, "Piscina", "Acceso a la piscina del hotel.", 0, "Bienestar", true));
        servicios.add(new Servicio(9, "Gimnasio", "Acceso al gimnasio equipado del hotel.", 0, "Bienestar", true));
        servicios.add(new Servicio(10, "Spa", "Circuito de relajación y tratamientos de spa.", 120000, "Bienestar", true));
        servicios.add(new Servicio(11, "Sauna", "Sesión de sauna para relajación.", 40000, "Bienestar", true));
        servicios.add(new Servicio(12, "Jacuzzi", "Acceso privado al jacuzzi durante una hora.", 60000, "Bienestar", true));
        servicios.add(new Servicio(13, "Masajes", "Masaje relajante de cuerpo completo.", 150000, "Bienestar", true));
        servicios.add(new Servicio(14, "Transporte al aeropuerto", "Traslado entre el hotel y el aeropuerto.", 90000, "Transporte", true));
        servicios.add(new Servicio(15, "Minibar", "Productos consumidos del minibar de la habitación.", 20000, "Alimentación", true));
        servicios.add(new Servicio(16, "Limpieza de habitación", "Limpieza adicional solicitada por el huésped.", 25000, "Habitación", true));
        servicios.add(new Servicio(17, "Guardaequipaje", "Custodia temporal de equipaje.", 0, "Atención al huésped", true));
        servicios.add(new Servicio(18, "Servicio despertador", "Llamada a la hora indicada por el huésped.", 0, "Atención al huésped", true));
        servicios.add(new Servicio(19, "Alquiler de bicicletas", "Préstamo de bicicleta por un día.", 50000, "Recreación", true));
        servicios.add(new Servicio(20, "Tours turísticos", "Recorrido guiado por los principales lugares de la ciudad.", 180000, "Recreación", true));
    }

    public List<Servicio> listarTodos() {
        return servicios;
    }
}
