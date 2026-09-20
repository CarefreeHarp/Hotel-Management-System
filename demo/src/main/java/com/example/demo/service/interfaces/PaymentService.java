package com.example.demo.service.interfaces;

import com.example.demo.entities.Payment;

/** Reglas de negocio para registrar pagos y sincronizar sus folios. */
public interface PaymentService {


    Payment create(Payment payment);

    Payment update(Integer paymentId, Payment payment);

    void removeOperatorFromPayments(Integer operatorId);

    void delete(Integer paymentId);
}
