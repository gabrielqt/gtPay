package com.gabrielqt.gtpay.exception;

public class MerchantWithoutBaseUrlException extends BusinessException {
  public MerchantWithoutBaseUrlException(Long merchantId) {
    super(
            "The merchant with id " + merchantId + " must have a base URL configured."
    );
  }
}
