package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.dto.request.ChargeRequest;
import com.gabrielqt.gtpay.dto.response.ChargeResponse;
import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.repository.ChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargeService {
    private final ChargeRepository chargeRepository;

    public ChargeResponse createCharge(ChargeRequest request) {
        return null;
    }
}
