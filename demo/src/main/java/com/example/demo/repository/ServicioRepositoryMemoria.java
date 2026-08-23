package com.example.demo.repository;

import com.example.demo.entitys.Servicio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ServicioRepositoryMemoria {

    private final List<Servicio> servicios = new ArrayList<>();

    public ServicioRepositoryMemoria() {
    servicios.add(new Servicio(10, "Spa", "spa", "Circuito de relajación y tratamientos de spa.", 120000, "Bienestar", true, "Relajación y bienestar en un entorno exclusivo", "90 minutos", "Todos los días · Sujeto a reserva", "Piso 2 · Área de Bienestar",
            "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1800&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1600334089648-b0d9d3028eb2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1519823551278-64ac92734fb1?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(1, "Wi-Fi", "wi-fi", "Acceso a internet inalámbrico de alta velocidad.", 0, "Conectividad", true, "Durante toda tu estadía", "24 horas", "Todos los días · Sujeto a reserva", "Todo el hotel",
            "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1497366754035-f200968a6e72?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1521737711867-e3b97375f902?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(2, "Desayuno", "desayuno", "Desayuno buffet servido en el restaurante.", 45000, "Alimentación", true, "Una mañana hecha a tu medida", "6:30 a.m. - 10:30 a.m.", "Todos los días · Sujeto a reserva", "Restaurante Atlan",
            "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1525351484163-7529414344d8?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1493770348161-369560ae357d?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1504754524776-8f4f37790ca0?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(6, "Lavandería", "lavanderia", "Lavado y planchado de prendas personales.", 30000, "Lavandería", true, "El cuidado que tus prendas merecen", "Entrega en 24 horas", "Todos los días · Sujeto a reserva", "Recepción y habitación",
            "https://images.unsplash.com/photo-1545173168-9f1947eebb7f?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1545173168-9f1947eebb7f?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1517677208171-0bc6725a3e60?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1610557892470-55d9e80c0bce?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(8, "Piscina", "piscina", "Acceso a la piscina del hotel.", 0, "Bienestar", true, "Un respiro frente al agua", "7:00 a.m. - 9:00 p.m.", "Todos los días · Sujeto a reserva", "Terraza nivel 3",
            "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(9, "Gimnasio", "gimnasio", "Acceso al gimnasio equipado del hotel.", 0, "Bienestar", true, "Energía para cada día", "5:00 a.m. - 11:00 p.m.", "Todos los días · Sujeto a reserva", "Nivel 2",
            "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1571902943202-507ec2618e8f?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(14, "Transporte al aeropuerto", "transporte-al-aeropuerto", "Traslado entre el hotel y el aeropuerto.", 90000, "Transporte", true, "Llegadas y salidas sin preocupaciones", "Bajo reserva", "Todos los días · Sujeto a reserva", "Aeropuerto y hotel",
            "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1483450388369-9ed95738483c?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(3, "Almuerzo", "almuerzo", "Menú del día con bebida incluida.", 55000, "Alimentación", true, "Sabores frescos a mediodía", "12:00 m. - 3:00 p.m.", "Todos los días · Sujeto a reserva", "Restaurante Atlan",
            "https://images.unsplash.com/photo-1515003197210-e0cd71810b5f?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1515003197210-e0cd71810b5f?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(4, "Cena", "cena", "Cena a la carta en el restaurante del hotel.", 65000, "Alimentación", true, "Una velada para recordar", "6:00 p.m. - 10:00 p.m.", "Todos los días · Sujeto a reserva", "Restaurante Atlan",
            "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1559339352-11d035aa65de?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(5, "Servicio a la habitación", "servicio-a-la-habitacion", "Entrega de alimentos y bebidas en la habitación.", 15000, "Alimentación", true, "Atención privada en tu suite", "24 horas", "Todos los días · Sujeto a reserva", "Tu habitación",
            "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(7, "Estacionamiento", "estacionamiento", "Espacio vigilado para el vehículo del huésped.", 25000, "Transporte", true, "Tranquilidad desde tu llegada", "24 horas", "Todos los días · Sujeto a reserva", "Nivel -1",
            "https://images.unsplash.com/photo-1506521781263-d8422e82f27a?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1506521781263-d8422e82f27a?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1590674899484-d5640e854abe?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1470224114660-3f6686c562eb?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1573348722427-f1d6819fdf98?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(11, "Sauna", "sauna", "Sesión de sauna para relajación.", 40000, "Bienestar", true, "Calor que renueva", "45 minutos", "Todos los días · Sujeto a reserva", "Piso 2 · Área de Bienestar",
            "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1600334089648-b0d9d3028eb2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1552693673-1bf958298935?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(12, "Jacuzzi", "jacuzzi", "Acceso privado al jacuzzi durante una hora.", 60000, "Bienestar", true, "Un momento solo para ti", "60 minutos", "Todos los días · Sujeto a reserva", "Piso 2 · Área de Bienestar",
            "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(13, "Masajes", "masajes", "Masaje relajante de cuerpo completo.", 150000, "Bienestar", true, "Equilibrio para cuerpo y mente", "60 minutos", "Todos los días · Sujeto a reserva", "Piso 2 · Área de Bienestar",
            "https://images.unsplash.com/photo-1519823551278-64ac92734fb1?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1519823551278-64ac92734fb1?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1600334089648-b0d9d3028eb2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(15, "Minibar", "minibar", "Productos consumidos del minibar de la habitación.", 20000, "Alimentación", true, "Pequeños placeres a cualquier hora", "24 horas", "Todos los días · Sujeto a reserva", "Tu habitación",
            "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1544145945-f90425340c7e?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1551024506-0bccd828d307?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1510626176961-4b57d4fbad03?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(16, "Limpieza de habitación", "limpieza-de-habitacion", "Limpieza adicional solicitada por el huésped.", 25000, "Habitación", true, "Tu espacio, siempre impecable", "Bajo solicitud", "Todos los días · Sujeto a reserva", "Tu habitación",
            "https://images.unsplash.com/photo-1584132967334-10e028bd69f7?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1584132967334-10e028bd69f7?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(17, "Guardaequipaje", "guardaequipaje", "Custodia temporal de equipaje.", 0, "Atención al huésped", true, "Disfruta la ciudad sin cargas", "24 horas", "Todos los días · Sujeto a reserva", "Recepción",
            "https://images.unsplash.com/photo-1553531889-56e0c9ed82e9?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1553531889-56e0c9ed82e9?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1569154941061-e231b4725ef1?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1542296332-2e4473faf563?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1499678329028-101435549a4e?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(18, "Servicio despertador", "servicio-despertador", "Llamada a la hora indicada por el huésped.", 0, "Atención al huésped", true, "Cada día comienza a tiempo", "24 horas", "Todos los días · Sujeto a reserva", "Recepción",
            "https://images.unsplash.com/photo-1501139083538-0139583c060f?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1501139083538-0139583c060f?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1533749047139-189de3cf06d3?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1495195134817-aeb325a55b65?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1484480974693-6ca0a78fb36b?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(19, "Alquiler de bicicletas", "alquiler-de-bicicletas", "Préstamo de bicicleta por un día.", 50000, "Recreación", true, "Descubre la ciudad a tu ritmo", "Por día", "Todos los días · Sujeto a reserva", "Lobby",
            "https://images.unsplash.com/photo-1502744688674-c619d1586c9e?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1502744688674-c619d1586c9e?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1529422643029-d4585747aaf2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1571068316344-75bc76f77890?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1541625602330-2277a4c46182?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));

    servicios.add(new Servicio(20, "Tours turísticos", "tours-turisticos", "Recorrido guiado por los principales lugares de la ciudad.", 180000, "Recreación", true, "Historias y lugares por descubrir", "Medio día", "Todos los días · Sujeto a reserva", "Lobby",
            "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1488646953014-85cb44e25828?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Atención personalizada", "Ambiente exclusivo Atlan Suites", "Reserva flexible", "Servicio para huéspedes")));
}

    public Servicio obtenerPorNombreUrl(String nombreUrl) {
        return servicios.stream()
                .filter(servicio -> servicio.getNombreUrl().equalsIgnoreCase(nombreUrl))
                .findFirst()
                .orElse(null);
    }
}
