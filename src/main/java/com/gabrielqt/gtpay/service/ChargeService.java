package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.dto.request.ChargeRequest;
import com.gabrielqt.gtpay.dto.response.ChargeResponse;
import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.enums.PaymentType;
import com.gabrielqt.gtpay.entity.enums.Status;
import com.gabrielqt.gtpay.exception.BusinessException;
import com.gabrielqt.gtpay.exception.ObjectNotFoundException;
import com.gabrielqt.gtpay.mapper.ChargeMapper;
import com.gabrielqt.gtpay.repository.ChargeRepository;
import com.gabrielqt.gtpay.service.helper.DateTimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChargeService {
    private final ChargeRepository chargeRepository;
    private final DateTimeProvider dateTimeProvider;
    private final BrCodeService brCodeService;
    private final ChargeMapper chargeMapper;


    @Transactional
    public ChargeResponse newCharge(ChargeRequest request,
                                    Merchant merchant) {

        existsChargeByExternalId(request.externalId(), merchant);
        Charge charge = Charge.builder()
                .status(Status.PENDING)
                .amount(request.amount())
                .externalId(request.externalId())
                .paymentType(request.paymentType())
                .merchant(merchant)
                .createdAt(dateTimeProvider.now())
                .expiresAt(dateTimeProvider.expiresInMinutes(5))
                .build();

        if(paymentTypeIsPix(request)) {
            charge.setBrCode(brCodeService.generateBrCode(merchant, charge));
        }

        chargeRepository.save(charge);
        return chargeMapper.toChargeResponse(charge);
    }

    public Charge findChargeByExternalId(String externalId) {
         return chargeRepository.findByExternalId(externalId)
                 .orElseThrow(() -> new ObjectNotFoundException("Charge not found"));
    }

    public void existsChargeByExternalId(String externalId, Merchant merchant) {
        if (chargeRepository.existsByExternalIdAndMerchant(externalId, merchant))
        {
            throw new BusinessException("Charge with external id for this merchant already exists");
        };
    }

    private boolean paymentTypeIsPix(ChargeRequest chargeRequest) {
        return chargeRequest.paymentType().equals(PaymentType.PIX);
    }

    public Charge findById(Long chargeId) {
        return chargeRepository.findById(chargeId)
                .orElseThrow(() -> new ObjectNotFoundException("Charge not found"));
    }

    public boolean isChargeExpired(Charge charge) {
        return dateTimeProvider.now().isAfter(charge.getExpiresAt());
    }

    public boolean isChargePending(Charge charge) {
        return charge.getStatus().equals(Status.PENDING);
    }
}

