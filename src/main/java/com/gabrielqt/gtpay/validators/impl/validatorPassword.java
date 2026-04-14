package com.gabrielqt.gtpay.validators.impl;

import com.gabrielqt.gtpay.validators.interfaces.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class validatorPassword implements ConstraintValidator <Password, String>{

    // valores passados na anottation:
    private int minLength;
    private boolean requireUppercase;
    private boolean requireSpecialChar;

    @Override
    public void initialize(Password constraintAnnotation) {
        // pega os valores passado pra anotação (roda só uma vez)
        this.minLength = constraintAnnotation.minLength();
        this.requireUppercase = constraintAnnotation.requireUppercase();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null || password.isBlank()) return false;

        if (password.length() < minLength){
            addConstraintMessage(context, "Password length should be at least " + minLength + " characters");
            return false;
        }

        if (requireSpecialChar && !password.matches(".*[!@#$%^&*()].*")){
            addConstraintMessage(context, "Password must have at least one special character");
            return false;
        }

        if (requireUppercase && !password.matches(".*[A-Z].*")){
            addConstraintMessage(context, "Password must have at least one uppercase character");
            return false;
        }

        return true;
    }

    // Substitui a mensagem default pela customizada
    private void addConstraintMessage(ConstraintValidatorContext context, String msg) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg)
                .addConstraintViolation();
    }
}
