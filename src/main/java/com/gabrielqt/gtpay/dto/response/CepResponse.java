package com.gabrielqt.gtpay.dto.response;

public record CepResponse(
        String cep,
        String logradouro,
        String complemento,
        String unidade,
        String bairro,
        String localidade,
        String uf,
        String estado,
        String regiao,
        String ibge,
        String ddd,
        String erro
) {
    public boolean isValid() {
        return erro == null && cep != null;
    }
}