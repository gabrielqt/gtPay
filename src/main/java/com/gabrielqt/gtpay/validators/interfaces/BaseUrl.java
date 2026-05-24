package com.gabrielqt.gtpay.validators.interfaces;

import com.gabrielqt.gtpay.validators.impl.validatorBaseUrl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = validatorBaseUrl.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseUrl {
    String message() default "Invalid URL, example valid url: 'https://endpoint.com.br'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
