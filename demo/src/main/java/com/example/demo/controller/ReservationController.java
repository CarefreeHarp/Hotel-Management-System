package com.example.demo.controller;

import com.example.demo.entities.Room;
import com.example.demo.entities.Folio;
import com.example.demo.entities.Reservation;
import com.example.demo.errors.InvalidPaymentDataException;
import com.example.demo.service.interfaces.FolioService;
import com.example.demo.service.interfaces.ReservationService;
import com.example.demo.service.interfaces.RoomService;
import com.example.demo.service.interfaces.ServiceService;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

/** Controla las pantallas iniciales del flujo de reservas. */
@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    RoomService roomService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    FolioService folioService;

    @Autowired
    Clock applicationClock;

    // Full URL: http://localhost:8080/reservation/book
    @GetMapping("/book")
    public String showBooking(HttpSession session, Model model) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "redirect:/admin/panel";
        }
        if (session.getAttribute("clientId") == null) {
            session.setAttribute("pendingClientDestination", "/reservation/book");
            return "redirect:/login";
        }
        model.addAttribute("applicationToday", LocalDate.now(applicationClock));
        return "reservations/booking";
    }

    // Full URL: http://localhost:8080/reservation/availability
    @GetMapping("/availability")
    public String showAvailability(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false, defaultValue = "1") Integer guests,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            Model model) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            return "redirect:/reservation/book";
        }
        Page<Room> availableRooms = roomService.listAvailableForStay(checkIn, checkOut, page);
        model.addAttribute("rooms", availableRooms.getContent());
        model.addAttribute("currentPage", availableRooms.getNumber());
        model.addAttribute("hasPreviousPage", availableRooms.hasPrevious());
        model.addAttribute("hasNextPage", availableRooms.hasNext());
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("guests", guests);
        return "reservations/availability";
    }

    // Full URL: http://localhost:8080/reservation/services
    @GetMapping("/services")
    public String showServiceSelection(
            @RequestParam(required = false) Integer roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false, defaultValue = "1") Integer guests,
            HttpSession session,
            Model model) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "redirect:/admin/panel";
        }
        if (session.getAttribute("clientId") == null) {
            session.setAttribute("pendingClientDestination", "/reservation/book");
            return "redirect:/login";
        }
        if (!hasValidStay(roomId, checkIn, checkOut)) {
            return "redirect:/reservation/book";
        }
        model.addAttribute("servicios", serviceService.listServices().stream()
                .filter(service -> Boolean.TRUE.equals(service.getActive()))
                .toList());
        model.addAttribute("room", roomService.findById(roomId));
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("guests", Math.max(1, guests));
        return "reservations/services-selection";
    }

    // Full URL: http://localhost:8080/reservation/services/select
    @PostMapping("/services/select")
    public String addSelectedServices(
            @RequestParam Integer roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(defaultValue = "1") Integer guests,
            @RequestParam(required = false) List<Integer> serviceIds,
            HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "redirect:/admin/panel";
        }
        if (session.getAttribute("clientId") == null || !hasValidStay(roomId, checkIn, checkOut)) {
            return "redirect:/reservation/book";
        }
        if (serviceIds == null || serviceIds.isEmpty()) {
            return reservationRedirect("/reservation/services", roomId, checkIn, checkOut, guests, List.of());
        }
        serviceService.getActiveServicesByIds(serviceIds);
        return reservationRedirect("/reservation/checkout", roomId, checkIn, checkOut, guests, serviceIds);
    }

    // Full URL: http://localhost:8080/reservation/checkout
    @GetMapping("/checkout")
    public String showCheckoutChoice(
            @RequestParam(required = false) Integer roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false, defaultValue = "1") Integer guests,
            @RequestParam(required = false) List<Integer> serviceIds,
            HttpSession session,
            Model model) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "redirect:/admin/panel";
        }
        if (session.getAttribute("clientId") == null || !hasValidStay(roomId, checkIn, checkOut)) {
            return "redirect:/reservation/book";
        }
        prepareReservationSummary(model, roomId, checkIn, checkOut, guests, serviceIds);
        return "reservations/checkout";
    }

    // Full URL: http://localhost:8080/reservation/checkout/book-later
    @PostMapping("/checkout/book-later")
    public String bookAndPayLater(
            @RequestParam Integer roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(defaultValue = "1") Integer guests,
            @RequestParam(required = false) List<Integer> serviceIds,
            HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "redirect:/admin/panel";
        }
        Object clientId = session.getAttribute("clientId");
        if (!(clientId instanceof Integer)) {
            return "redirect:/login";
        }
        var reservation = reservationService.createPendingReservation(
                (Integer) clientId, roomId, checkIn, checkOut, guests, serviceIds);
        return "redirect:/reservation/confirmation?reservationId=" + reservation.getReservationId() + "&paymentAmount=0";
    }

    // Full URL: http://localhost:8080/reservation/payment
    @GetMapping("/payment")
    public String showPayment(
            @RequestParam(required = false) Integer roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false, defaultValue = "1") Integer guests,
            @RequestParam(required = false) List<Integer> serviceIds,
            HttpSession session,
            Model model) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "redirect:/admin/panel";
        }
        if (session.getAttribute("clientId") == null || !hasValidStay(roomId, checkIn, checkOut)) {
            return "redirect:/reservation/book";
        }
        preparePaymentForm(model, roomId, checkIn, checkOut, guests, serviceIds);
        return "reservations/payment";
    }

    // Full URL: http://localhost:8080/reservation/payment/confirm
    @PostMapping("/payment/confirm")
    public String confirmPayment(
            @RequestParam Integer roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(defaultValue = "1") Integer guests,
            @RequestParam(required = false) String paymentAmount,
            @RequestParam(required = false) String cardholderName,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) String securityCode,
            @RequestParam(required = false) List<Integer> serviceIds,
            HttpSession session,
            Model model) {
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return "redirect:/admin/panel";
        }
        Object clientId = session.getAttribute("clientId");
        if (!(clientId instanceof Integer)) {
            return "redirect:/login";
        }
        if (roomId == null || checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            return "redirect:/reservation/book";
        }
        Reservation reservation;
        try {
            reservation = reservationService.createReservationWithCardPayment(
                    (Integer) clientId,
                    roomId,
                    checkIn,
                    checkOut,
                    guests,
                    paymentAmount,
                    cardholderName,
                    cardNumber,
                    expiryDate,
                    securityCode,
                    serviceIds);
        } catch (InvalidPaymentDataException exception) {
            preparePaymentForm(model, roomId, checkIn, checkOut, guests, serviceIds);
            model.addAttribute("paymentAmount", paymentAmount);
            model.addAttribute("cardholderName", cardholderName);
            model.addAttribute("cardNumber", cardNumber);
            model.addAttribute("expiryDate", expiryDate);
            model.addAttribute("securityCode", securityCode);
            model.addAttribute("error", exception.getMessage());
            return "reservations/payment";
        }
        return "redirect:/reservation/confirmation?reservationId=" + reservation.getReservationId()
                + "&paymentAmount=" + paymentAmount;
    }

    // Full URL: http://localhost:8080/reservation/confirmation
    @GetMapping("/confirmation")
    public String showConfirmation(
            @RequestParam(required = false) Integer reservationId,
            @RequestParam(required = false) BigDecimal paymentAmount,
            Model model) {
        if (reservationId == null) {
            return "redirect:/reservation/book";
        }
        Folio folio = folioService.findByReservationId(reservationId);
        var reservation = folio.getReservation();
        BigDecimal amountPaidToday = paymentAmount == null ? BigDecimal.ZERO : paymentAmount;

        model.addAttribute("room", reservation.getRoom());
        model.addAttribute("checkIn", reservation.getCheckInDate());
        model.addAttribute("checkOut", reservation.getCheckOutDate());
        model.addAttribute("guests", reservation.getGuestCount());
        model.addAttribute("nights", ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate()));
        model.addAttribute("serviceItems", folioService.listItemsByFolioId(folio.getFolioId()));
        model.addAttribute("total", folio.getTotal());
        model.addAttribute("paymentAmount", amountPaidToday);
        model.addAttribute("paymentRecorded", amountPaidToday.compareTo(BigDecimal.ZERO) > 0);
        model.addAttribute("reservationConfirmed", amountPaidToday.compareTo(folio.getTotal()) >= 0);
        return "reservations/confirmation";
    }

    /** Prepara los datos que el formulario de pago necesita para mostrarse o reintentarse. */
    private void preparePaymentForm(
            Model model, Integer roomId, LocalDate checkIn, LocalDate checkOut, Integer guests, List<Integer> serviceIds) {
        prepareReservationSummary(model, roomId, checkIn, checkOut, guests, serviceIds);
    }

    /** Obtiene desde la base de datos todos los importes que el flujo debe mostrar. */
    private void prepareReservationSummary(
            Model model, Integer roomId, LocalDate checkIn, LocalDate checkOut, Integer guests, List<Integer> serviceIds) {
        Room room = roomService.findById(roomId);
        BigDecimal nightlyPrice = room.getRoomType().getNightlyPrice();
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal stayTotal = nightlyPrice.multiply(BigDecimal.valueOf(nights));
        var selectedServices = serviceService.getActiveServicesByIds(serviceIds);
        BigDecimal servicesTotal = selectedServices.stream()
                .map(service -> service.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("room", room);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("guests", Math.max(1, guests));
        model.addAttribute("nights", nights);
        model.addAttribute("stayTotal", stayTotal);
        model.addAttribute("selectedServices", selectedServices);
        model.addAttribute("servicesTotal", servicesTotal);
        model.addAttribute("serviceIds", serviceIds == null ? List.of() : serviceIds);
        model.addAttribute("reservationQuery", reservationQuery(roomId, checkIn, checkOut, guests, serviceIds));
        model.addAttribute("total", stayTotal.add(servicesTotal));
    }

    /** Determina si los parámetros mínimos describen una estancia válida. */
    private boolean hasValidStay(Integer roomId, LocalDate checkIn, LocalDate checkOut) {
        return roomId != null && checkIn != null && checkOut != null && checkOut.isAfter(checkIn);
    }

    /** Construye una redirección sin usar sesión: todo el borrador queda visible en la URL. */
    private String reservationRedirect(
            String path, Integer roomId, LocalDate checkIn, LocalDate checkOut, Integer guests, List<Integer> serviceIds) {
        return "redirect:" + UriComponentsBuilder.fromPath(path)
                .query(reservationQuery(roomId, checkIn, checkOut, guests, serviceIds))
                .toUriString();
    }

    /** Serializa los parámetros de la reserva, conservando un serviceIds por cada servicio elegido. */
    private String reservationQuery(
            Integer roomId, LocalDate checkIn, LocalDate checkOut, Integer guests, List<Integer> serviceIds) {
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .queryParam("roomId", roomId)
                .queryParam("checkIn", checkIn)
                .queryParam("checkOut", checkOut)
                .queryParam("guests", Math.max(1, guests));
        if (serviceIds != null) {
            serviceIds.forEach(serviceId -> builder.queryParam("serviceIds", serviceId));
        }
        return builder.build().encode().getQuery();
    }

}
