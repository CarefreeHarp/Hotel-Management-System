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
        RoomType standardRoom = saveRoomType(
                "Standard Room",
                "A comfortable room with a queen bed, work desk and private bathroom.",
                new BigDecimal("250000"),
                2,
                "https://images.unsplash.com/photo-1631049307264-da0ec9d70304",
                List.of("https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea", "https://images.unsplash.com/photo-1566665797739-1674de7a421a"));

        RoomType deluxeRoom = saveRoomType(
                "Deluxe Room",
                "A spacious room with a king bed, lounge area and panoramic city views.",
                new BigDecimal("360000"),
                2,
                "https://images.unsplash.com/photo-1590490360182-c33d57733427",
                List.of("https://images.unsplash.com/photo-1616486338812-3dadae4b4ace", "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0"));

        RoomType executiveRoom = saveRoomType(
                "Executive Room",
                "A refined room with a dedicated workspace, premium amenities and lounge access.",
                new BigDecimal("450000"),
                3,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b",
                List.of("https://images.unsplash.com/photo-1618773928121-c32242e63f39", "https://images.unsplash.com/photo-1595576508898-0ad5c879a061"));

        RoomType familySuite = saveRoomType(
                "Family Suite",
                "A two-room suite designed for families, with extra beds and a generous living area.",
                new BigDecimal("590000"),
                5,
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a",
                List.of("https://images.unsplash.com/photo-1564078516393-cf04bd966897", "https://images.unsplash.com/photo-1584132967334-10e028bd69f7"));

        RoomType presidentialSuite = saveRoomType(
                "Presidential Suite",
                "Our signature suite with a private terrace, jacuzzi and personalized guest service.",
                new BigDecimal("950000"),
                6,
                "https://images.unsplash.com/photo-1600566753086-00f18fb6b3ea",
                List.of("https://images.unsplash.com/photo-1600607687920-4e2a09cf159d", "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c"));

        // Registro reservado para mantener reservas cuyo cuarto físico fue eliminado.
        jdbcTemplate.update(
                "insert into room (room_id, room_number, floor, status, room_type_id) values (-1, -1, 0, ?, ?)",
                RoomStatus.MAINTENANCE.name(), standardRoom.getRoomTypeId());

        // Clientes
        saveClient("Emma", "Thompson", "1001001001", "3001001001",
                "emma.thompson@example.com", "Emma2026!", "https://images.unsplash.com/photo-1494790108377-be9c29b29330");
        saveClient("Liam", "Anderson", "1001001002", "3001001002",
                "liam.anderson@example.com", "Liam2026!", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e");
        saveClient("Olivia", "Martinez", "1001001003", "3001001003",
                "olivia.martinez@example.com", "Olivia2026!", "https://images.unsplash.com/photo-1534528741775-53994a69daeb");
        saveClient("Noah", "Williams", "1001001004", "3001001004",
                "noah.williams@example.com", "Noah2026!", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d");
        saveClient("Ava", "Robinson", "1001001005", "3001001005",
                "ava.robinson@example.com", "Ava2026!", "https://images.unsplash.com/photo-1517841905240-472988babdf9");
        saveClient("Ethan", "Walker", "1001001006", "3001001006",
                "ethan.walker@example.com", "Ethan2026!", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7");
        saveClient("Sophia", "Harris", "1001001007", "3001001007",
                "sophia.harris@example.com", "Sophia2026!", "https://images.unsplash.com/photo-1531123897727-8f129e1688ce");
        saveClient("Lucas", "Clark", "1001001008", "3001001008",
                "lucas.clark@example.com", "Lucas2026!", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d");
        saveClient("Mia", "Lewis", "1001001009", "3001001009",
                "mia.lewis@example.com", "Mia2026!", "https://images.unsplash.com/photo-1524504388940-b1c1722653e1");
        saveClient("James", "Young", "1001001010", "3001001010",
                "james.young@example.com", "James2026!", "https://images.unsplash.com/photo-1507591064344-4c6ce005b128");

        // Habitaciones
        saveRoom(101, 1, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(102, 1, RoomStatus.AVAILABLE, deluxeRoom);
        saveRoom(103, 1, RoomStatus.OCCUPIED, executiveRoom);
        saveRoom(104, 1, RoomStatus.AVAILABLE, familySuite);
        saveRoom(105, 1, RoomStatus.MAINTENANCE, presidentialSuite);
        saveRoom(106, 1, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(107, 1, RoomStatus.OCCUPIED, deluxeRoom);
        saveRoom(108, 1, RoomStatus.AVAILABLE, executiveRoom);
        saveRoom(109, 1, RoomStatus.AVAILABLE, familySuite);
        saveRoom(110, 1, RoomStatus.OCCUPIED, presidentialSuite);

        saveRoom(201, 2, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(202, 2, RoomStatus.OCCUPIED, deluxeRoom);
        saveRoom(203, 2, RoomStatus.AVAILABLE, executiveRoom);
        saveRoom(204, 2, RoomStatus.AVAILABLE, familySuite);
        saveRoom(205, 2, RoomStatus.MAINTENANCE, presidentialSuite);
        saveRoom(206, 2, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(207, 2, RoomStatus.OCCUPIED, deluxeRoom);
        saveRoom(208, 2, RoomStatus.AVAILABLE, executiveRoom);
        saveRoom(209, 2, RoomStatus.AVAILABLE, familySuite);
        saveRoom(210, 2, RoomStatus.OCCUPIED, presidentialSuite);

        saveRoom(301, 3, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(302, 3, RoomStatus.AVAILABLE, deluxeRoom);
        saveRoom(303, 3, RoomStatus.OCCUPIED, executiveRoom);
        saveRoom(304, 3, RoomStatus.AVAILABLE, familySuite);
        saveRoom(305, 3, RoomStatus.MAINTENANCE, presidentialSuite);
        saveRoom(306, 3, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(307, 3, RoomStatus.OCCUPIED, deluxeRoom);
        saveRoom(308, 3, RoomStatus.AVAILABLE, executiveRoom);
        saveRoom(309, 3, RoomStatus.AVAILABLE, familySuite);
        saveRoom(310, 3, RoomStatus.OCCUPIED, presidentialSuite);

        saveRoom(401, 4, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(402, 4, RoomStatus.OCCUPIED, deluxeRoom);
        saveRoom(403, 4, RoomStatus.AVAILABLE, executiveRoom);
        saveRoom(404, 4, RoomStatus.AVAILABLE, familySuite);
        saveRoom(405, 4, RoomStatus.MAINTENANCE, presidentialSuite);
        saveRoom(406, 4, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(407, 4, RoomStatus.OCCUPIED, deluxeRoom);
        saveRoom(408, 4, RoomStatus.AVAILABLE, executiveRoom);
        saveRoom(409, 4, RoomStatus.AVAILABLE, familySuite);
        saveRoom(410, 4, RoomStatus.OCCUPIED, presidentialSuite);

        saveRoom(501, 5, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(502, 5, RoomStatus.AVAILABLE, deluxeRoom);
        saveRoom(503, 5, RoomStatus.OCCUPIED, executiveRoom);
        saveRoom(504, 5, RoomStatus.AVAILABLE, familySuite);
        saveRoom(505, 5, RoomStatus.MAINTENANCE, presidentialSuite);
        saveRoom(506, 5, RoomStatus.AVAILABLE, standardRoom);
        saveRoom(507, 5, RoomStatus.OCCUPIED, deluxeRoom);
        saveRoom(508, 5, RoomStatus.AVAILABLE, executiveRoom);
        saveRoom(509, 5, RoomStatus.AVAILABLE, familySuite);
        saveRoom(510, 5, RoomStatus.OCCUPIED, presidentialSuite);

        // Servicios
        // Registro reservado para conservar cargos cuyo servicio original fue eliminado.
        jdbcTemplate.update(
                "insert into service (service_id, name, url_name, description, price, category, active, main_image_url) values (-1, ?, ?, ?, ?, ?, ?, ?)",
                "Deleted service", "deleted-service", "System record for deleted services.",
                BigDecimal.ZERO, "System", false, "about:blank");

        saveService("High-speed Wi-Fi", "high-speed-wi-fi",
                "Complimentary high-speed wireless internet throughout the hotel.", new BigDecimal("0"),
                "Connectivity", true, "Stay connected throughout your visit", "24 hours",
                "Available every day", "Entire hotel",
                "https://images.unsplash.com/photo-1496181133206-80ce9b88a853");

        saveService("Breakfast Buffet", "breakfast-buffet",
                "A generous breakfast buffet with local produce, fresh fruit and baked goods.", new BigDecimal("45000"),
                "Food and beverage", true, "A fresh start prepared every morning", "6:30 AM - 10:30 AM",
                "Available every day", "Atlan Restaurant",
                "https://images.unsplash.com/photo-1533089860892-a7c6f0a88666");

        saveService("Lunch Menu", "lunch-menu",
                "A seasonal lunch menu prepared with fresh regional ingredients.", new BigDecimal("55000"),
                "Food and beverage", true, "Fresh flavors for the middle of your day", "12:00 PM - 3:00 PM",
                "Available every day", "Atlan Restaurant",
                "https://images.unsplash.com/photo-1515003197210-e0cd71810b5f");

        saveService("Fine Dining", "fine-dining",
                "An elegant à la carte dinner featuring contemporary Colombian cuisine.", new BigDecimal("65000"),
                "Food and beverage", true, "An evening designed to be remembered", "6:00 PM - 10:00 PM",
                "Available every day", "Atlan Restaurant",
                "https://images.unsplash.com/photo-1414235077428-338989a2e8c0");

        saveService("Room Service", "room-service",
                "Meals and beverages delivered directly to the comfort of your room.", new BigDecimal("15000"),
                "Room", true, "Private dining whenever you need it", "24 hours",
                "Available every day", "Guest rooms",
                "https://images.unsplash.com/photo-1564501049412-61c2a3083791");

        saveService("Laundry Service", "laundry-service",
                "Professional washing, drying and ironing for personal garments.", new BigDecimal("30000"),
                "Laundry", true, "Thoughtful care for every garment", "Delivery within 24 hours",
                "Available every day", "Front desk and guest rooms",
                "https://images.unsplash.com/photo-1545173168-9f1947eebb7f");

        saveService("Secure Parking", "secure-parking",
                "Monitored parking available for hotel guests.", new BigDecimal("25000"),
                "Transportation", true, "Peace of mind from the moment you arrive", "24 hours",
                "Subject to availability", "Basement level",
                "https://images.unsplash.com/photo-1506521781263-d8422e82f27a");

        saveService("Swimming Pool", "swimming-pool",
                "Access to the hotel's temperature-controlled panoramic pool.", new BigDecimal("0"),
                "Wellness", true, "A quiet pause beside the water", "7:00 AM - 9:00 PM",
                "Available every day", "Third-floor terrace",
                "https://images.unsplash.com/photo-1566073771259-6a8506099945");

        saveService("Fitness Center", "fitness-center",
                "A modern fitness center equipped for cardio and strength training.", new BigDecimal("0"),
                "Wellness", true, "Keep your energy moving every day", "5:00 AM - 11:00 PM",
                "Available every day", "Second floor",
                "https://images.unsplash.com/photo-1534438327276-14e5300c3a48");

        saveService("Signature Spa", "signature-spa",
                "A restorative wellness circuit with personalized spa treatments.", new BigDecimal("120000"),
                "Wellness", true, "Relaxation in an exclusive setting", "90 minutes",
                "Reservation required", "Second-floor wellness area",
                "https://images.unsplash.com/photo-1540555700478-4be289fbecef");

        saveService("Sauna Session", "sauna-session",
                "A private sauna session designed to relax the body and clear the mind.", new BigDecimal("40000"),
                "Wellness", true, "Restorative warmth and complete calm", "45 minutes",
                "Reservation required", "Second-floor wellness area",
                "https://images.unsplash.com/photo-1544161515-4ab6ce6db874");

        saveService("Private Jacuzzi", "private-jacuzzi",
                "One hour of private access to the hotel jacuzzi.", new BigDecimal("60000"),
                "Wellness", true, "A peaceful moment reserved for you", "60 minutes",
                "Reservation required", "Second-floor wellness area",
                "https://images.unsplash.com/photo-1578683010236-d716f9a3f461");

        saveService("Relaxing Massage", "relaxing-massage",
                "A full-body massage performed by an experienced wellness therapist.", new BigDecimal("150000"),
                "Wellness", true, "Balance for body and mind", "60 minutes",
                "Reservation required", "Second-floor wellness area",
                "https://images.unsplash.com/photo-1519823551278-64ac92734fb1");

        saveService("Airport Transfer", "airport-transfer",
                "Private transportation between the hotel and the airport.", new BigDecimal("90000"),
                "Transportation", true, "Arrivals and departures without stress", "By reservation",
                "Available every day", "Airport and hotel",
                "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2");

        saveService("In-room Minibar", "in-room-minibar",
                "A curated selection of drinks and snacks available in your room.", new BigDecimal("20000"),
                "Food and beverage", true, "Small pleasures at any hour", "24 hours",
                "Available every day", "Guest rooms",
                "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd");

        saveService("Additional Housekeeping", "additional-housekeeping",
                "Additional room cleaning requested outside the regular housekeeping schedule.", new BigDecimal("25000"),
                "Room", true, "Your space, refreshed whenever you need it", "By request",
                "Available every day", "Guest rooms",
                "https://images.unsplash.com/photo-1584132967334-10e028bd69f7");

        saveService("Luggage Storage", "luggage-storage",
                "Secure temporary luggage storage before check-in or after check-out.", new BigDecimal("0"),
                "Guest services", true, "Explore the city without carrying your bags", "24 hours",
                "Available every day", "Front desk",
                "https://images.unsplash.com/photo-1569154941061-e231b4725ef1");

        saveService("Wake-up Call", "wake-up-call",
                "A personalized telephone wake-up call at your requested time.", new BigDecimal("0"),
                "Guest services", true, "Begin every day right on time", "24 hours",
                "Available every day", "Front desk",
                "https://images.unsplash.com/photo-1501139083538-0139583c060f");

        saveService("Bicycle Rental", "bicycle-rental",
                "A city bicycle available for independent exploration throughout the day.", new BigDecimal("50000"),
                "Recreation", true, "Discover the city at your own pace", "Full day",
                "Subject to availability", "Lobby",
                "https://images.unsplash.com/photo-1502744688674-c619d1586c9e");

        saveService("Guided City Tour", "guided-city-tour",
                "A guided visit to the city's most memorable landmarks and neighborhoods.", new BigDecimal("180000"),
                "Recreation", true, "Stories and places waiting to be discovered", "Half day",
                "Reservation required", "Lobby",
                "https://images.unsplash.com/photo-1469474968028-56623f02e42e");

        serviceRepository.findAll().stream()
                .filter(service -> Boolean.TRUE.equals(service.getActive()))
                .forEach(service -> {
                    service.setDescription(buildDetailedDescription(service));
                    service.setSecondaryImageUrls(new ArrayList<>(serviceGalleryImages()));
                });
    }

    /** Guarda un tipo de habitación creado mediante su builder. */
    private RoomType saveRoomType(String name, String description, BigDecimal nightlyPrice,
                                  Integer maxCapacity, String mainPhoto, List<String> secondaryPhotos) {
        return roomTypeRepository.save(RoomType.builder()
                .name(name)
                .description(description)
                .nightlyPrice(nightlyPrice)
                .maxCapacity(maxCapacity)
                .mainPhoto(mainPhoto)
                .secondaryPhotos(new ArrayList<>(secondaryPhotos))
                .build());
    }

    /** Guarda un cliente creado mediante su builder. */
    private void saveClient(String name, String lastName, String nationalId, String phone,
                            String email, String password, String profilePhoto) {
        clientRepository.save(Client.builder()
                .name(name)
                .lastName(lastName)
                .nationalId(nationalId)
                .phone(phone)
                .email(email)
                .password(password)
                .profilePhoto(profilePhoto)
                .build());
    }

    /** Guarda una habitación creada mediante su builder. */
    private void saveRoom(Integer number, Integer floor, RoomStatus status, RoomType roomType) {
        roomRepository.save(Room.builder()
                .number(number)
                .floor(floor)
                .status(status)
                .roomType(roomType)
                .build());
    }

    /** Guarda un servicio creado mediante su builder. */
    private void saveService(String name, String urlName, String description, BigDecimal price,
                             String category, Boolean active, String summary, String duration,
                             String availability, String location, String mainImageUrl) {
        serviceRepository.save(Service.builder()
                .name(name)
                .urlName(urlName)
                .description(description)
                .price(price)
                .category(category)
                .active(active)
                .summary(summary)
                .duration(duration)
                .availability(availability)
                .location(location)
                .mainImageUrl(mainImageUrl)
                .secondaryImageUrls(new ArrayList<>())
                .build());
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
