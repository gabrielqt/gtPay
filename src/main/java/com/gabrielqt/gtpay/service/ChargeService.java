package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.dto.request.ChargeRequest;
import com.gabrielqt.gtpay.dto.response.ChargeResponse;
import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.enums.Status;
import com.gabrielqt.gtpay.exception.BusinessException;
import com.gabrielqt.gtpay.exception.ObjectNotFoundException;
import com.gabrielqt.gtpay.repository.ChargeRepository;
import com.gabrielqt.gtpay.service.helper.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargeService {
    private final ChargeRepository chargeRepository;
    private final DateTimeProvider dateTimeProvider;


    public void newCharge(ChargeRequest request,
                                    Merchant merchant) {

        existsChargeByExternalId(request.externalId(), merchant);
        Charge charge = Charge.builder()
                .status(Status.PENDING)
                .amount(request.amount())
                .payment(null)
                .externalId(request.externalId())
                .merchant(merchant)
                .createdAt(dateTimeProvider.now())
                .expiresAt(dateTimeProvider.expiresInMinutes(5))
                .build();
        chargeRepository.save(charge);
    }

    public Charge findChargeByExternalId(Long externalId) {
         return chargeRepository.findByExternalId(externalId)
                 .orElseThrow(() -> new ObjectNotFoundException("Charge not found"));
    }

    public void existsChargeByExternalId(Long externalId, Merchant merchant) {
        if (chargeRepository.existsByExternalIdAndMerchant(externalId, merchant))
        {
            throw new BusinessException("Charge with external id for this merchant already exists");
        };
    }

}
