package com.gabrielqt.gtpay.exception;

public class EmailAlreadyRegisteredException extends BusinessException {
    public EmailAlreadyRegisteredException(String email) {
        super("Email already registered: " + email);
    }
}
