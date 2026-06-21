package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
