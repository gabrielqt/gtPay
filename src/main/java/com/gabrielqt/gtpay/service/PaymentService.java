package com.gabrielqt.gtpay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final String PAYLOAD_FORMAT = "01";
    private final String MERCHANT_CATEGORY = "0000";
    private final String CURRENCY_CODE = "986"; // moeda
    private final String COUNTRY = "BR";
    private final String DOMAIN = "br.gov.bcb.pix";

    public String generateBrCode(){
        return "Oi";
    }

    private String field(String codeField, String value){
        String length = String.format("%02d", value.length());
        return codeField + length + value;
    }
}



