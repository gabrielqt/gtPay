package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.entity.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookService {

    public void dispatchWebhook(Charge charge){}
}
