package com.example.demo.service;

import com.example.demo.entities.Client;
import com.example.demo.entities.Folio;
import com.example.demo.entities.FolioItem;
import com.example.demo.entities.Payment;
import com.example.demo.entities.Reservation;
import com.example.demo.entities.Room;
import com.example.demo.entities.Service;
import com.example.demo.entities.enums.FolioStatus;
import com.example.demo.entities.enums.ReservationStatus;
import com.example.demo.entities.enums.PaymentStatus;
import com.example.demo.errors.InvalidPaymentDataException;
import com.example.demo.repository.FolioRepository;
import com.example.demo.repository.FolioItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.service.interfaces.ClientService;
import com.example.demo.service.interfaces.ReservationService;
import com.example.demo.service.interfaces.RoomService;
import com.example.demo.service.interfaces.PaymentService;
import com.example.demo.service.interfaces.ServiceService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.example.demo.dto.ClientReservationDetailDTO;
import com.example.demo.repository.PaymentRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

/** Implementa el vencimiento automático de reservas pendientes. */
@org.springframework.stereotype.Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private FolioRepository folioRepository;

    @Autowired
    private FolioItemRepository folioItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private Clock applicationClock;

    /** Crea una reserva pendiente junto con el folio inicial que recibirá pagos posteriores. */
    @Override
    @Transactional
    public Reservation createPendingReservation(
            Integer clientId, Integer roomId, LocalDate checkInDate, LocalDate checkOutDate, Integer guests,
            List<Integer> serviceIds) {
        return createReservationAndFolio(clientId, roomId, checkInDate, checkOutDate, guests, serviceIds)
                .getReservation();
    }

    /** Crea una reserva y registra un pago de tarjeta que puede cubrir solo parte del folio. */
    @Override
    @Transactional
    public Reservation createReservationWithCardPayment(
            Integer clientId,
            Integer roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Integer guests,
            String paymentAmount,
            String cardholderName,
            String cardNumber,
            String expiryDate,
            String securityCode,
            List<Integer> serviceIds) {
        Folio folio = createReservationAndFolio(clientId, roomId, checkInDate, checkOutDate, guests, serviceIds);
        BigDecimal validatedPaymentAmount = validateCardPaymentData(
                paymentAmount, folio.getTotal(), cardholderName, cardNumber, expiryDate, securityCode);
        paymentService.create(Payment.builder()
                .folio(folio)
                .amount(validatedPaymentAmount)
                .paymentMethod("CARD")
                .status(PaymentStatus.APPROVED)
                .paidAt(LocalDateTime.now(applicationClock))
                .build());
        return folio.getReservation();
    }

    /** Valida los campos del formulario de pago antes de persistir la reserva y su abono. */
    private BigDecimal validateCardPaymentData(
            String paymentAmount,
            BigDecimal total,
            String cardholderName,
            String cardNumber,
            String expiryDate,
            String securityCode) {
        if (paymentAmount == null || paymentAmount.isBlank()) {
            throw new InvalidPaymentDataException(InvalidPaymentDataException.Reason.PAYMENT_AMOUNT_REQUIRED, null);
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(paymentAmount.trim());
        } catch (NumberFormatException exception) {
            throw new InvalidPaymentDataException(InvalidPaymentDataException.Reason.PAYMENT_AMOUNT_INVALID,
                    paymentAmount);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentDataException(InvalidPaymentDataException.Reason.PAYMENT_AMOUNT_INVALID, amount);
        }
        if (amount.compareTo(total) > 0) {
            throw new InvalidPaymentDataException(InvalidPaymentDataException.Reason.PAYMENT_AMOUNT_EXCEEDS_TOTAL,
                    amount);
        }
        if (cardholderName == null || cardholderName.isBlank()) {
            throw new InvalidPaymentDataException(InvalidPaymentDataException.Reason.CARDHOLDER_NAME_REQUIRED, null);
        }
        if (cardNumber == null || !cardNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}")) {
            throw new InvalidPaymentDataException(InvalidPaymentDataException.Reason.CARD_NUMBER_INVALID, cardNumber);
        }
        if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            throw new InvalidPaymentDataException(InvalidPaymentDataException.Reason.EXPIRY_DATE_INVALID, expiryDate);
        }
        if (securityCode == null || !securityCode.matches("\\d{3}")) {
            throw new InvalidPaymentDataException(InvalidPaymentDataException.Reason.SECURITY_CODE_INVALID, securityCode);
        }
        return amount;
    }

    /** Crea el encabezado de la reserva pendiente y el folio inicial asociado a ella. */
    private Folio createReservationAndFolio(
            Integer clientId, Integer roomId, LocalDate checkInDate, LocalDate checkOutDate, Integer guests,
            List<Integer> serviceIds) {
        if (clientId == null || roomId == null || checkInDate == null || checkOutDate == null
                || !checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("The reservation data is invalid.");
        }

        Client client = clientService.findById(clientId);
        Room room = roomService.findById(roomId);
        int guestCount = Math.max(1, guests == null ? 1 : guests);
        if (guestCount > room.getRoomType().getMaxCapacity()) {
            throw new IllegalArgumentException("The guest count exceeds the room capacity.");
        }

        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal stayTotal = room.getRoomType().getNightlyPrice().multiply(BigDecimal.valueOf(nights));
        List<Service> selectedServices = serviceService.getActiveServicesByIds(serviceIds);
        BigDecimal servicesTotal = selectedServices.stream()
                .map(Service::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal estimatedTotal = stayTotal.add(servicesTotal);
        Reservation reservation = Reservation.builder()
                .reservationCode("ATL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .guestCount(guestCount)
                .nightlyPrice(room.getRoomType().getNightlyPrice())
                .estimatedTotal(estimatedTotal)
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now(applicationClock))
                .client(client)
                .room(room)
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);
        Folio folio = folioRepository.save(Folio.builder()
                .reservation(savedReservation)
                .subtotal(estimatedTotal)
                .taxes(BigDecimal.ZERO)
                .total(estimatedTotal)
                .status(FolioStatus.PENDING)
                .issuedAt(LocalDateTime.now(applicationClock))
                .build());
        folioItemRepository.saveAll(selectedServices.stream()
                .map(service -> FolioItem.builder()
                        .folio(folio)
                        .service(service)
                        .concept(service.getName())
                        .unitPrice(service.getPrice())
                        .quantity(1)
                        .subtotal(service.getPrice())
                        .chargedAt(LocalDateTime.now(applicationClock))
                        .build())
                .toList());
        return folio;
    }

    /** Cancela reservas pendientes vencidas y sincroniza el estado de ocupación de las habitaciones. */
    @Override
    @Transactional
    public int runMidnightReservationAndRoomStatusActions() {
        List<Reservation> expiredReservations = reservationRepository
                .findByStatusAndCheckInDateBefore(ReservationStatus.PENDING, LocalDate.now(applicationClock));
        expiredReservations.forEach(reservation -> reservation.setStatus(ReservationStatus.CANCELLED));
        reservationRepository.saveAll(expiredReservations);
        roomService.synchronizeOccupancyStatus();
        return expiredReservations.size();
    }

    /** Obtiene el historial completo de reservas de un cliente con sus folios, servicios y pagos asociados. */
    @Override
    public List<ClientReservationDetailDTO> getClientReservationHistory(Integer clientId) {
        if (clientId == null) {
            return Collections.emptyList();
        }
        List<Reservation> reservations = reservationRepository.findByClient_ClientIdOrderByCreatedAtDesc(clientId);
        List<ClientReservationDetailDTO> history = new ArrayList<>();
        for (Reservation reservation : reservations) {
            Folio folio = folioRepository.findByReservationReservationId(reservation.getReservationId()).orElse(null);
            List<FolioItem> items = folio != null
                    ? folioItemRepository.findByFolioFolioIdOrderByChargedAtAsc(folio.getFolioId())
                    : Collections.emptyList();
            List<Payment> payments = folio != null
                    ? paymentRepository.findByFolioFolioIdOrderByPaidAtAsc(folio.getFolioId())
                    : Collections.emptyList();

            history.add(ClientReservationDetailDTO.builder()
                    .reservation(reservation)
                    .folio(folio)
                    .folioItems(items)
                    .payments(payments)
                    .build());
        }
        return history;
    }
}
