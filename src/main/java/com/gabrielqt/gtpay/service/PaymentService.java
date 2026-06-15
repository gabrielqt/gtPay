package com.gabrielqt.gtpay.service;

import com.gabrielqt.gtpay.entity.Charge;
import com.gabrielqt.gtpay.entity.Merchant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
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

        String payload =
            field("01", PAYLOAD_FORMAT) +
            field("26", pixInfo) +
            field("52", MERCHANT_CATEGORY) +
            field("53", CURRENCY_CODE) +
            field("54", amount) +
            field("58", COUNTRY) +
            field("59", sanitizeForBrCode(merchant.getStoreName(), 25)) +
            field("60", sanitizeForBrCode(merchant.getCity(), 15)) +
            field("62", field("05", charge.getExternalId())) +
            "6304"; // campo 63 (CRC) + comprimento "04", sem valor ainda

        return payload + crc16(payload);
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

    private String crc16(String payload) {
        // seed inicial definido pelo padrão CRC-16/CCITT-FALSE
        int crc = 0xFFFF;

        for (byte b : payload.getBytes(StandardCharsets.UTF_8)) {
            // coloca o byte na parte alta do registrador de 16 bits (XOR com os 8 bits superiores)
            crc ^= (b & 0xFF) << 8;

            for (int i = 0; i < 8; i++) {
                // se o bit mais significativo está setado, aplica o polinômio 0x1021 (CCITT)
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    // senão apenas desloca um bit à esquerda
                    crc <<= 1;
                }
            }

            // garante que o resultado fique em 16 bits (descarta o overflow)
            crc &= 0xFFFF;
        }

        // formata como 4 caracteres hexadecimais maiúsculos, ex: "A3F2"
        return String.format("%04X", crc);
    }

    private String sanitizeForBrCode(String text, int maxLength) {
        String clean = removeAccents(text)
                .toUpperCase()
                .trim();

        return clean.length() > maxLength
                ? clean.substring(0, maxLength)
                : clean;
    }
}



