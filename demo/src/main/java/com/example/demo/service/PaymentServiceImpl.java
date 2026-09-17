package com.example.demo.service;

import com.example.demo.entities.Folio;
import com.example.demo.entities.Payment;
import com.example.demo.entities.enums.FolioStatus;
import com.example.demo.entities.enums.PaymentStatus;
import com.example.demo.entities.enums.ReservationStatus;
import com.example.demo.errors.ResourceNotFoundException;
import com.example.demo.repository.FolioRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.interfaces.PaymentService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Implementa el registro de pagos y la actualización de su folio. */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private FolioRepository folioRepository;

    /** Registra un pago y recalcula el estado del folio dentro de una transacción. */
    @Override
    @Transactional
    public Payment create(Payment payment) {
        Folio folio = getFolioFromPayment(payment);
        payment.setPaymentId(null);
        payment.setFolio(folio);
        Payment savedPayment = paymentRepository.save(payment);
        recalculateFolioStatus(folio);
        return savedPayment;
    }

    /** Actualiza un pago y recalcula todos los folios que resulten afectados. */
    @Override
    @Transactional
    public Payment update(Integer paymentId, Payment payment) {
        Payment registeredPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("The payment does not exist."));
        Folio previousFolio = registeredPayment.getFolio();
        Folio currentFolio = getFolioFromPayment(payment);

        payment.setPaymentId(registeredPayment.getPaymentId());
        payment.setFolio(currentFolio);
        Payment savedPayment = paymentRepository.save(payment);
        recalculateFolioStatus(previousFolio);
        if (!previousFolio.getFolioId().equals(currentFolio.getFolioId())) {
            recalculateFolioStatus(currentFolio);
        }
        return savedPayment;
    }

    /** Elimina un pago y recalcula el estado del folio al que pertenecía. */
    @Override
    @Transactional
    public void delete(Integer paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("The payment does not exist."));
        Folio folio = payment.getFolio();
        paymentRepository.delete(payment);
        paymentRepository.flush();
        recalculateFolioStatus(folio);
    }

    /** Obtiene el folio válido indicado por el pago recibido. */
    private Folio getFolioFromPayment(Payment payment) {
        if (payment.getFolio() == null || payment.getFolio().getFolioId() == null) {
            throw new IllegalArgumentException("A payment must belong to a folio.");
        }
        return folioRepository.findById(payment.getFolio().getFolioId())
                .orElseThrow(() -> new ResourceNotFoundException("The folio does not exist."));
    }

    /** Actualiza el folio y confirma su reserva cuando los pagos aprobados lo cubren. */
    private void recalculateFolioStatus(Folio folio) {
        BigDecimal approvedTotal = paymentRepository.sumAmountsByFolioIdAndStatus(
                folio.getFolioId(), PaymentStatus.APPROVED);
        FolioStatus status = approvedTotal.compareTo(folio.getTotal()) >= 0
                ? FolioStatus.PAID
                : FolioStatus.PENDING;
        folio.setStatus(status);
        if (status == FolioStatus.PAID) {
            folio.getReservation().setStatus(ReservationStatus.CONFIRMED);
        }
        folioRepository.save(folio);
    }
}
