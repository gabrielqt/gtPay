package com.gabrielqt.gtpay.repository;

import com.gabrielqt.gtpay.entity.Charge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
}
