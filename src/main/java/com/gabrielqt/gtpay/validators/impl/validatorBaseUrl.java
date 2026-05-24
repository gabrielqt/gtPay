package com.gabrielqt.gtpay.validators.impl;

import com.gabrielqt.gtpay.validators.interfaces.BaseUrl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;
import java.net.URISyntaxException;

public class validatorBaseUrl implements ConstraintValidator<BaseUrl, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        if (value.endsWith("/")){
            return false;
        }

        try {
            URI uri = new URI(value);
            // need to be https
            if (uri.getScheme() == null ||
                    (!uri.getScheme().equalsIgnoreCase("https"))) {
                return false;
            }
            // valid host
            return uri.getHost() != null
                    && !uri.getHost().isBlank();

        } catch (URISyntaxException e) {
            return false;
        }
    }
}
