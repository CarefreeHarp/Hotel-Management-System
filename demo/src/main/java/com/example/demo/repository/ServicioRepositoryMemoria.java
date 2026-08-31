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
    servicios.add(new Servicio(10, "Spa", "spa", "Relaxation circuit and spa treatments.", 120000, "Wellness", true, "Relaxation and wellness in an exclusive setting", "90 minutes", "Every day · Reservation required", "Level 2 · Wellness area",
            "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1800&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1600334089648-b0d9d3028eb2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1519823551278-64ac92734fb1?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(1, "Wi-Fi", "wi-fi", "High-speed wireless internet access.", 0, "Connectivity", true, "Throughout your stay", "24 hours", "Every day · Reservation required", "Entire hotel",
            "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1497366754035-f200968a6e72?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1521737711867-e3b97375f902?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(2, "Breakfast", "desayuno", "Buffet breakfast served in the restaurant.", 45000, "Food and beverage", true, "A morning made for you", "6:30 a.m. - 10:30 a.m.", "Every day · Reservation required", "Atlan restaurant",
            "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1525351484163-7529414344d8?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1493770348161-369560ae357d?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1504754524776-8f4f37790ca0?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(6, "Laundry", "lavanderia", "Washing and ironing for personal garments.", 30000, "Laundry", true, "The care your garments deserve", "24-hour delivery", "Every day · Reservation required", "Front desk and room",
            "https://images.unsplash.com/photo-1545173168-9f1947eebb7f?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1545173168-9f1947eebb7f?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1517677208171-0bc6725a3e60?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1610557892470-55d9e80c0bce?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(8, "Pool", "piscina", "Access to the hotel pool.", 0, "Wellness", true, "A breath of fresh air by the water", "7:00 a.m. - 9:00 p.m.", "Every day · Reservation required", "Level 3 terrace",
            "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(9, "Gym", "gimnasio", "Access to the hotel's fully equipped gym.", 0, "Wellness", true, "Energy for every day", "5:00 a.m. - 11:00 p.m.", "Every day · Reservation required", "Level 2",
            "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1571902943202-507ec2618e8f?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(14, "Airport transfer", "transporte-al-aeropuerto", "Transfer between the hotel and the airport.", 90000, "Transportation", true, "Worry-free arrivals and departures", "By reservation", "Every day · Reservation required", "Airport and hotel",
            "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1483450388369-9ed95738483c?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(3, "Lunch", "almuerzo", "Daily menu with a drink included.", 55000, "Food and beverage", true, "Fresh flavors at midday", "12:00 p.m. - 3:00 p.m.", "Every day · Reservation required", "Atlan restaurant",
            "https://images.unsplash.com/photo-1515003197210-e0cd71810b5f?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1515003197210-e0cd71810b5f?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(4, "Dinner", "cena", "À la carte dinner in the hotel restaurant.", 65000, "Food and beverage", true, "An evening to remember", "6:00 p.m. - 10:00 p.m.", "Every day · Reservation required", "Atlan restaurant",
            "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1550966871-3ed3cdb5ed0c?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1559339352-11d035aa65de?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(5, "Room service", "servicio-a-la-habitacion", "Food and beverage delivery to your room.", 15000, "Food and beverage", true, "Private service in your suite", "24 hours", "Every day · Reservation required", "Your room",
            "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(7, "Parking", "estacionamiento", "Secure parking space for guest vehicles.", 25000, "Transportation", true, "Peace of mind from arrival", "24 hours", "Every day · Reservation required", "Level -1",
            "https://images.unsplash.com/photo-1506521781263-d8422e82f27a?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1506521781263-d8422e82f27a?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1590674899484-d5640e854abe?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1470224114660-3f6686c562eb?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1573348722427-f1d6819fdf98?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(11, "Sauna", "sauna", "Sauna session for relaxation.", 40000, "Wellness", true, "Rejuvenating warmth", "45 minutes", "Every day · Reservation required", "Level 2 · Wellness area",
            "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1600334089648-b0d9d3028eb2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1552693673-1bf958298935?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(12, "Jacuzzi", "jacuzzi", "Private access to the jacuzzi for one hour.", 60000, "Wellness", true, "A moment just for you", "60 minutes", "Every day · Reservation required", "Level 2 · Wellness area",
            "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(13, "Massage", "masajes", "Relaxing full-body massage.", 150000, "Wellness", true, "Balance for body and mind", "60 minutes", "Every day · Reservation required", "Level 2 · Wellness area",
            "https://images.unsplash.com/photo-1519823551278-64ac92734fb1?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1519823551278-64ac92734fb1?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1600334089648-b0d9d3028eb2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(15, "Minibar", "minibar", "Products available from the in-room minibar.", 20000, "Food and beverage", true, "Small pleasures at any hour", "24 hours", "Every day · Reservation required", "Your room",
            "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1544145945-f90425340c7e?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1551024506-0bccd828d307?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1510626176961-4b57d4fbad03?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(16, "Room cleaning", "limpieza-de-habitacion", "Additional cleaning requested by the guest.", 25000, "Room", true, "Your space, always immaculate", "On request", "Every day · Reservation required", "Your room",
            "https://images.unsplash.com/photo-1584132967334-10e028bd69f7?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1584132967334-10e028bd69f7?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(17, "Luggage storage", "guardaequipaje", "Temporary luggage storage.", 0, "Guest services", true, "Enjoy the city hands-free", "24 hours", "Every day · Reservation required", "Front desk",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQAEK_yuZnkl4EALT_rVsnvWPCtC-CxFQVhGPiEN9J5Qg&s",
            List.of(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQAEK_yuZnkl4EALT_rVsnvWPCtC-CxFQVhGPiEN9J5Qg&s",
                    "https://images.unsplash.com/photo-1569154941061-e231b4725ef1?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1542296332-2e4473faf563?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1499678329028-101435549a4e?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(18, "Wake-up call", "servicio-despertador", "Call at the time requested by the guest.", 0, "Guest services", true, "Every day starts on time", "24 hours", "Every day · Reservation required", "Front desk",
            "https://images.unsplash.com/photo-1501139083538-0139583c060f?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1501139083538-0139583c060f?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1533749047139-189de3cf06d3?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1495195134817-aeb325a55b65?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1484480974693-6ca0a78fb36b?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(19, "Bike rental", "alquiler-de-bicicletas", "Bicycle rental for one day.", 50000, "Recreation", true, "Discover the city at your own pace", "Per day", "Every day · Reservation required", "Lobby",
            "https://images.unsplash.com/photo-1502744688674-c619d1586c9e?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1502744688674-c619d1586c9e?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1529422643029-d4585747aaf2?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1571068316344-75bc76f77890?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1541625602330-2277a4c46182?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));

    servicios.add(new Servicio(20, "City tours", "tours-turisticos", "Guided tour of the city's main landmarks.", 180000, "Recreation", true, "Stories and places to discover", "Half day", "Every day · Reservation required", "Lobby",
            "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1600&q=85",
            List.of(
                    "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1488646953014-85cb44e25828?auto=format&fit=crop&w=1200&q=85",
                    "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=85"
            ),
            List.of("Personalized attention", "Exclusive Atlan Suites setting", "Flexible booking", "Guest service")));
}

    public List<Servicio> listarTodos() {
        return new ArrayList<>(servicios);
    }

    public Servicio obtenerPorNombreUrl(String nombreUrl) {
        return servicios.stream()
                .filter(servicio -> servicio.getNombreUrl().equalsIgnoreCase(nombreUrl))
                .findFirst()
                .orElse(null);
    }
}
