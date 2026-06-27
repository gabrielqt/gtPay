package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.config.RabbitMQConfig;
import com.gabrielqt.gtpay.dto.message.MessageCharge;
import com.gabrielqt.gtpay.dto.request.SimulatePaymentRequest;
import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.Payment;
import com.gabrielqt.gtpay.entity.PaymentCard;
import com.gabrielqt.gtpay.entity.WebhookSubscription;
import com.gabrielqt.gtpay.entity.enums.PaymentResult;
import com.gabrielqt.gtpay.entity.enums.Status;
import com.gabrielqt.gtpay.exception.BusinessException;
import com.gabrielqt.gtpay.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ChargeService chargeService;
    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void receivePaymentFromPsp(SimulatePaymentRequest request) {
        Charge charge = chargeService.findById(request.chargeId());
        if(!chargeService.isChargePending(charge)) {throw new BusinessException("Just charges with status PENDING can be paid or failed.");}

        if(chargeService.isChargeExpired(charge)){
            charge.setStatus(Status.EXPIRED);
            sendMessage(charge);
            return;
        }

        charge.setStatus(request.result().equals(PaymentResult.APPROVED) ? Status.PAID : Status.FAILED);
        if(request.result().equals(PaymentResult.APPROVED)){Payment payment = buildPayment(charge, request); charge.setPayment(payment);}

        sendMessage(charge);
    }

    private Payment buildPayment(Charge charge, SimulatePaymentRequest request) {
        return switch (charge.getPaymentType()) {
            case PIX -> paymentRepository.save(
                    Payment.builder()
                            .amount(charge.getAmount())
                            .createdAt(LocalDateTime.now())
                            .charge(charge)
                            .build()
            );

            case CARD -> paymentRepository.save(
                    PaymentCard.builder()
                            .amount(charge.getAmount())
                            .createdAt(LocalDateTime.now())
                            .charge(charge)
                            .lastDigits(request.cardLastDigits())
                            .cardBrand(request.cardBrand())
                            .holderName(request.cardHolder())
                            .build()
            );
        };
    }

    private void sendMessage(Charge charge){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CHARGE_EXCHANGE,
                new MessageCharge(
                        charge.getId(),
                        charge.getExternalId(),
                        charge.getMerchant().getId(),
                        charge.getStatus(),
                        LocalDateTime.now()
                )
        );
    }
}



