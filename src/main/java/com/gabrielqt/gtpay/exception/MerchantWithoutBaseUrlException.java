package com.gabrielqt.gtpay.exception;

public class MerchantWithoutBaseUrlException extends BusinessException {
  public MerchantWithoutBaseUrlException() {
    super(
            "The merchant must have a base URL configured."
    );
  }
}
