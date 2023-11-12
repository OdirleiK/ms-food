package br.com.mspayments.payments.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mspayments.payments.model.Payment;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
