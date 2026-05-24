package com.gabrielqt.gtpay.exception;

public class MerchantAndPathAlreadyExists extends BusinessException {
  public MerchantAndPathAlreadyExists(String path, Long merchantId) {
    super(
            "The path: '" + path + "' already exists for merchant with id:" + merchantId
            );
  }
}
