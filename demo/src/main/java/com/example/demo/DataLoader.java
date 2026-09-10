package com.example.demo;

import com.example.demo.entities.Client;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomType;
import com.example.demo.entities.Service;
import com.example.demo.entities.enums.RoomStatus;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.RoomTypeRepository;
import com.example.demo.repository.ServiceRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {

        // Tipos de habitación
        RoomType standardRoom = roomTypeRepository.save(new RoomType(
                "Standard Room",
                "A comfortable room with a queen bed, work desk and private bathroom.",
                new BigDecimal("250000"),
                2));

        RoomType deluxeRoom = roomTypeRepository.save(new RoomType(
                "Deluxe Room",
                "A spacious room with a king bed, lounge area and panoramic city views.",
                new BigDecimal("360000"),
                2));

        RoomType executiveRoom = roomTypeRepository.save(new RoomType(
                "Executive Room",
                "A refined room with a dedicated workspace, premium amenities and lounge access.",
                new BigDecimal("450000"),
                3));

        RoomType familySuite = roomTypeRepository.save(new RoomType(
                "Family Suite",
                "A two-room suite designed for families, with extra beds and a generous living area.",
                new BigDecimal("590000"),
                5));

        RoomType presidentialSuite = roomTypeRepository.save(new RoomType(
                "Presidential Suite",
                "Our signature suite with a private terrace, jacuzzi and personalized guest service.",
                new BigDecimal("950000"),
                6));

        // Registro reservado para mantener reservas cuyo cuarto físico fue eliminado.
        jdbcTemplate.update(
                "insert into room (room_id, room_number, floor, status, room_type_id, main_photo) values (-1, -1, 0, ?, ?, ?)",
                RoomStatus.MAINTENANCE.name(), standardRoom.getRoomTypeId(), "about:blank");

        // Clientes
        clientRepository.save(new Client("Emma", "Thompson", "1001001001", "3001001001",
                "emma.thompson@example.com", "Emma2026!", "https://images.unsplash.com/photo-1494790108377-be9c29b29330"));
        clientRepository.save(new Client("Liam", "Anderson", "1001001002", "3001001002",
                "liam.anderson@example.com", "Liam2026!", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e"));
        clientRepository.save(new Client("Olivia", "Martinez", "1001001003", "3001001003",
                "olivia.martinez@example.com", "Olivia2026!", "https://images.unsplash.com/photo-1534528741775-53994a69daeb"));
        clientRepository.save(new Client("Noah", "Williams", "1001001004", "3001001004",
                "noah.williams@example.com", "Noah2026!", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d"));
        clientRepository.save(new Client("Ava", "Robinson", "1001001005", "3001001005",
                "ava.robinson@example.com", "Ava2026!", "https://images.unsplash.com/photo-1517841905240-472988babdf9"));
        clientRepository.save(new Client("Ethan", "Walker", "1001001006", "3001001006",
                "ethan.walker@example.com", "Ethan2026!", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7"));
        clientRepository.save(new Client("Sophia", "Harris", "1001001007", "3001001007",
                "sophia.harris@example.com", "Sophia2026!", "https://images.unsplash.com/photo-1531123897727-8f129e1688ce"));
        clientRepository.save(new Client("Lucas", "Clark", "1001001008", "3001001008",
                "lucas.clark@example.com", "Lucas2026!", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d"));
        clientRepository.save(new Client("Mia", "Lewis", "1001001009", "3001001009",
                "mia.lewis@example.com", "Mia2026!", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1"));
        clientRepository.save(new Client("James", "Young", "1001001010", "3001001010",
                "james.young@example.com", "James2026!", "https://images.unsplash.com/photo-1507591064344-4c6ce005b128"));

        // Habitaciones
        roomRepository.save(new Room(101, 1, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(102, 1, RoomStatus.AVAILABLE, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(103, 1, RoomStatus.OCCUPIED, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(104, 1, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(105, 1, RoomStatus.MAINTENANCE, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));
        roomRepository.save(new Room(106, 1, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(107, 1, RoomStatus.OCCUPIED, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(108, 1, RoomStatus.AVAILABLE, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(109, 1, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(110, 1, RoomStatus.OCCUPIED, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));

        roomRepository.save(new Room(201, 2, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(202, 2, RoomStatus.OCCUPIED, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(203, 2, RoomStatus.AVAILABLE, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(204, 2, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(205, 2, RoomStatus.MAINTENANCE, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));
        roomRepository.save(new Room(206, 2, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(207, 2, RoomStatus.OCCUPIED, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(208, 2, RoomStatus.AVAILABLE, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(209, 2, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(210, 2, RoomStatus.OCCUPIED, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));

        roomRepository.save(new Room(301, 3, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(302, 3, RoomStatus.AVAILABLE, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(303, 3, RoomStatus.OCCUPIED, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(304, 3, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(305, 3, RoomStatus.MAINTENANCE, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));
        roomRepository.save(new Room(306, 3, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(307, 3, RoomStatus.OCCUPIED, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(308, 3, RoomStatus.AVAILABLE, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(309, 3, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(310, 3, RoomStatus.OCCUPIED, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));

        roomRepository.save(new Room(401, 4, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(402, 4, RoomStatus.OCCUPIED, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(403, 4, RoomStatus.AVAILABLE, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(404, 4, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(405, 4, RoomStatus.MAINTENANCE, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));
        roomRepository.save(new Room(406, 4, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(407, 4, RoomStatus.OCCUPIED, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(408, 4, RoomStatus.AVAILABLE, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(409, 4, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(410, 4, RoomStatus.OCCUPIED, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));

        roomRepository.save(new Room(501, 5, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(502, 5, RoomStatus.AVAILABLE, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(503, 5, RoomStatus.OCCUPIED, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(504, 5, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(505, 5, RoomStatus.MAINTENANCE, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));
        roomRepository.save(new Room(506, 5, RoomStatus.AVAILABLE, standardRoom,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304", List.of()));
        roomRepository.save(new Room(507, 5, RoomStatus.OCCUPIED, deluxeRoom,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427", List.of()));
        roomRepository.save(new Room(508, 5, RoomStatus.AVAILABLE, executiveRoom,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b", List.of()));
        roomRepository.save(new Room(509, 5, RoomStatus.AVAILABLE, familySuite,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a", List.of()));
        roomRepository.save(new Room(510, 5, RoomStatus.OCCUPIED, presidentialSuite,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", List.of()));

        // Servicios
        // Registro reservado para conservar cargos cuyo servicio original fue eliminado.
        jdbcTemplate.update(
                "insert into service (service_id, name, url_name, description, price, category, active, main_image_url) values (-1, ?, ?, ?, ?, ?, ?, ?)",
                "Deleted service", "deleted-service", "System record for deleted services.",
                BigDecimal.ZERO, "System", false, "about:blank");

        serviceRepository.save(new Service("High-speed Wi-Fi", "high-speed-wi-fi",
                "Complimentary high-speed wireless internet throughout the hotel.", new BigDecimal("0"),
                "Connectivity", true, "Stay connected throughout your visit", "24 hours",
                "Available every day", "Entire hotel",
                "https://images.unsplash.com/photo-1496181133206-80ce9b88a853"));

        serviceRepository.save(new Service("Breakfast Buffet", "breakfast-buffet",
                "A generous breakfast buffet with local produce, fresh fruit and baked goods.", new BigDecimal("45000"),
                "Food and beverage", true, "A fresh start prepared every morning", "6:30 AM - 10:30 AM",
                "Available every day", "Atlan Restaurant",
                "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666"));

        serviceRepository.save(new Service("Lunch Menu", "lunch-menu",
                "A seasonal lunch menu prepared with fresh regional ingredients.", new BigDecimal("55000"),
                "Food and beverage", true, "Fresh flavors for the middle of your day", "12:00 PM - 3:00 PM",
                "Available every day", "Atlan Restaurant",
                "https://images.unsplash.com/photo-1515003197210-e0cd71810b5f"));

        serviceRepository.save(new Service("Fine Dining", "fine-dining",
                "An elegant à la carte dinner featuring contemporary Colombian cuisine.", new BigDecimal("65000"),
                "Food and beverage", true, "An evening designed to be remembered", "6:00 PM - 10:00 PM",
                "Available every day", "Atlan Restaurant",
                "https://images.unsplash.com/photo-1414235077428-338989a2e8c0"));

        serviceRepository.save(new Service("Room Service", "room-service",
                "Meals and beverages delivered directly to the comfort of your room.", new BigDecimal("15000"),
                "Room", true, "Private dining whenever you need it", "24 hours",
                "Available every day", "Guest rooms",
                "https://images.unsplash.com/photo-1564501049412-61c2a3083791"));

        serviceRepository.save(new Service("Laundry Service", "laundry-service",
                "Professional washing, drying and ironing for personal garments.", new BigDecimal("30000"),
                "Laundry", true, "Thoughtful care for every garment", "Delivery within 24 hours",
                "Available every day", "Front desk and guest rooms",
                "https://images.unsplash.com/photo-1545173168-9f1947eebb7f"));

        serviceRepository.save(new Service("Secure Parking", "secure-parking",
                "Monitored parking available for hotel guests.", new BigDecimal("25000"),
                "Transportation", true, "Peace of mind from the moment you arrive", "24 hours",
                "Subject to availability", "Basement level",
                "https://images.unsplash.com/photo-1506521781263-d8422e82f27a"));

        serviceRepository.save(new Service("Swimming Pool", "swimming-pool",
                "Access to the hotel's temperature-controlled panoramic pool.", new BigDecimal("0"),
                "Wellness", true, "A quiet pause beside the water", "7:00 AM - 9:00 PM",
                "Available every day", "Third-floor terrace",
                "https://images.unsplash.com/photo-1566073771259-6a8506099945"));

        serviceRepository.save(new Service("Fitness Center", "fitness-center",
                "A modern fitness center equipped for cardio and strength training.", new BigDecimal("0"),
                "Wellness", true, "Keep your energy moving every day", "5:00 AM - 11:00 PM",
                "Available every day", "Second floor",
                "https://images.unsplash.com/photo-1534438327276-14e5300c3a48"));

        serviceRepository.save(new Service("Signature Spa", "signature-spa",
                "A restorative wellness circuit with personalized spa treatments.", new BigDecimal("120000"),
                "Wellness", true, "Relaxation in an exclusive setting", "90 minutes",
                "Reservation required", "Second-floor wellness area",
                "https://images.unsplash.com/photo-1540555700478-4be289fbecef"));

        serviceRepository.save(new Service("Sauna Session", "sauna-session",
                "A private sauna session designed to relax the body and clear the mind.", new BigDecimal("40000"),
                "Wellness", true, "Restorative warmth and complete calm", "45 minutes",
                "Reservation required", "Second-floor wellness area",
                "https://images.unsplash.com/photo-1544161515-4ab6ce6db874"));

        serviceRepository.save(new Service("Private Jacuzzi", "private-jacuzzi",
                "One hour of private access to the hotel jacuzzi.", new BigDecimal("60000"),
                "Wellness", true, "A peaceful moment reserved for you", "60 minutes",
                "Reservation required", "Second-floor wellness area",
                "https://images.unsplash.com/photo-1578683010236-d716f9a3f461"));

        serviceRepository.save(new Service("Relaxing Massage", "relaxing-massage",
                "A full-body massage performed by an experienced wellness therapist.", new BigDecimal("150000"),
                "Wellness", true, "Balance for body and mind", "60 minutes",
                "Reservation required", "Second-floor wellness area",
                "https://images.unsplash.com/photo-1519823551278-64ac92734fb1"));

        serviceRepository.save(new Service("Airport Transfer", "airport-transfer",
                "Private transportation between the hotel and the airport.", new BigDecimal("90000"),
                "Transportation", true, "Arrivals and departures without stress", "By reservation",
                "Available every day", "Airport and hotel",
                "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2"));

        serviceRepository.save(new Service("In-room Minibar", "in-room-minibar",
                "A curated selection of drinks and snacks available in your room.", new BigDecimal("20000"),
                "Food and beverage", true, "Small pleasures at any hour", "24 hours",
                "Available every day", "Guest rooms",
                "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd"));

        serviceRepository.save(new Service("Additional Housekeeping", "additional-housekeeping",
                "Additional room cleaning requested outside the regular housekeeping schedule.", new BigDecimal("25000"),
                "Room", true, "Your space, refreshed whenever you need it", "By request",
                "Available every day", "Guest rooms",
                "https://images.unsplash.com/photo-1584132967334-10e028bd69f7"));

        serviceRepository.save(new Service("Luggage Storage", "luggage-storage",
                "Secure temporary luggage storage before check-in or after check-out.", new BigDecimal("0"),
                "Guest services", true, "Explore the city without carrying your bags", "24 hours",
                "Available every day", "Front desk",
                "https://images.unsplash.com/photo-1569154941061-e231b4725ef1"));

        serviceRepository.save(new Service("Wake-up Call", "wake-up-call",
                "A personalized telephone wake-up call at your requested time.", new BigDecimal("0"),
                "Guest services", true, "Begin every day right on time", "24 hours",
                "Available every day", "Front desk",
                "https://images.unsplash.com/photo-1501139083538-0139583c060f"));

        serviceRepository.save(new Service("Bicycle Rental", "bicycle-rental",
                "A city bicycle available for independent exploration throughout the day.", new BigDecimal("50000"),
                "Recreation", true, "Discover the city at your own pace", "Full day",
                "Subject to availability", "Lobby",
                "https://images.unsplash.com/photo-1502744688674-c619d1586c9e"));

        serviceRepository.save(new Service("Guided City Tour", "guided-city-tour",
                "A guided visit to the city's most memorable landmarks and neighborhoods.", new BigDecimal("180000"),
                "Recreation", true, "Stories and places waiting to be discovered", "Half day",
                "Reservation required", "Lobby",
                "https://images.unsplash.com/photo-1469474968028-56623f02e42e"));

        serviceRepository.findAll().stream()
                .filter(service -> Boolean.TRUE.equals(service.getActive()))
                .forEach(service -> {
                    service.setDescription(buildDetailedDescription(service));
                    service.setSecondaryImageUrls(new ArrayList<>(serviceGalleryImages()));
                });
    }

    /** Amplía cada descripción de servicio para que la página de detalle tenga contenido informativo. */
    private String buildDetailedDescription(Service service) {
        return service.getDescription()
                + "\n\nAtlan Suites designs this experience around a calm, attentive stay. "
                + service.getSummary() + " Our team prepares the service with the same care given to every guest request, "
                + "so the experience feels personal whether you are visiting for business, rest, or a special occasion."
                + "\n\nThe service is offered at " + service.getLocation() + " and is "
                + service.getAvailability().toLowerCase() + ". Its usual duration is " + service.getDuration()
                + ", and our concierge can help coordinate any details before or during your stay.";
    }

    /** Devuelve tres imágenes complementarias para la galería de cada servicio activo. */
    private List<String> serviceGalleryImages() {
        return List.of(
                "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb",
                "https://images.unsplash.com/photo-1564501049412-61c2a3083791",
                "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa");
    }
}
