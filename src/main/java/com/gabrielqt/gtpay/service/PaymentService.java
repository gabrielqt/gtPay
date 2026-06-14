package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.Normalizer;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final String PAYLOAD_FORMAT = "01";
    private final String MERCHANT_CATEGORY = "0000";
    private final String CURRENCY_CODE = "986"; // moeda
    private final String COUNTRY = "BR";
    private final String DOMAIN = "br.gov.bcb.pix";

    public String generateBrCode(Merchant merchant, Charge charge) {
        String pixInfo = field("00", DOMAIN) + field("01", merchant.getPixKey());
        String amount = charge.getAmount().setScale(2, RoundingMode.HALF_UP).toPlainString();

        return
            field("01", PAYLOAD_FORMAT) +
            field("26", pixInfo) +
            field("52", MERCHANT_CATEGORY) +
            field("53", CURRENCY_CODE) +
            field("54", amount) +
            field("58", COUNTRY) +
            field("59", sanitizeForBrCode(merchant.getStoreName(), 25)) +
            field("60", sanitizeForBrCode(merchant.getCity(), 15)) +
            field("62", field("05", charge.getExternalId()));
    }

    private String field(String codeField, String value){
        String length = String.format("%02d", value.length());
        return codeField + length + value;
    }

    private String removeAccents(String text) {
        // -> NFD separa "ã" em "a" + "~" (caractere base + acento)
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        // remove os caracteres de acento (categoria Unicode "Mark")
        return normalized.replaceAll("\\p{M}", "");
    }

    private String sanitizeForBrCode(String text, int maxLength) {
        String clean = removeAccents(text)
                .toUpperCase()
                .trim();

        // (cidade max 15, nome max 25 no padrão)
        return clean.length() > maxLength
                ? clean.substring(0, maxLength)
                : clean;
    }
}



