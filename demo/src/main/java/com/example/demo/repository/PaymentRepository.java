package com.example.demo.repository;

import com.example.demo.entities.Payment;
import com.example.demo.entities.enums.PaymentStatus;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** Acceso a pagos y a sus totales aprobados por folio. */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {


    /* Suma los pagos aprobados registrados para el folio indicado. */
    @Query("SELECT COALESCE(SUM(payment.amount), 0) FROM Payment payment "
            + "WHERE payment.folio.folioId = :folioId AND payment.status = :status")
    BigDecimal sumAmountsByFolioIdAndStatus(Integer folioId, PaymentStatus status);
    java.util.List<Payment> findByOperator_OperatorId(Integer operatorId);
}
