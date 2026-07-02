package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.config.RabbitMQConfig;
import com.gabrielqt.gtpay.dto.message.MessageCharge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final MerchantService merchantService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void onPaymentConfirmed (MessageCharge message) {
        String email = merchantService.findMerchantById(message.merchantId()).getUser().getEmail();
        sendPaymentEmail(email, message);
    }

    private void sendPaymentEmail(String toEmail, MessageCharge message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(toEmail);
            mail.setSubject("Pagamento " + message.status());
            mail.setText("Sua cobrança " + message.chargeId() +
                    " mudou para o status " + message.status() + ".");

            mailSender.send(mail);
            log.info("Email sent to {} for charge {}", toEmail, message.chargeId());
        } catch (MailException e) {
            log.warn("Failed to send email to {}: {}", toEmail, e.getMessage());
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", toEmail, e.getMessage());
        }
    }
}