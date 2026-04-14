package com.gabrielqt.gtpay.validators.interfaces;

import com.gabrielqt.gtpay.validators.impl.validatorPassword;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = validatorPassword.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "Invalid Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // atributo customizado -> passado para o validator
    int minLength() default 8;
    boolean requireUppercase() default true;
    boolean requireSpecialChar() default true;
}