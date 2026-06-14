package com.gabrielqt.gtpay.service.helper;


import com.gabrielqt.gtpay.dto.response.CepResponse;
import com.gabrielqt.gtpay.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CepFinder {

    private final RestClient restClient;

    public String findCityByCep(String cep) {
        return findCep(cep).localidade();
    }

    private String getUrl(String cep){
        return "https://viacep.com.br/ws/" + cep + "/json/";
    }

    public CepResponse findCep(String cep){
        CepResponse response =
                restClient.get()
                        .uri(getUrl(cep))
                        .retrieve()
                        .body(CepResponse.class);

        if(!response.isValid()){
            throw new BusinessException("Invalid CEP");
        }

        return response;
    }
}
