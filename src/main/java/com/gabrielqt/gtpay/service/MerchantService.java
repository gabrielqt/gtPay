package com.gabrielqt.gtpay.service;


import com.gabrielqt.gtpay.dto.response.MerchantResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.exception.ObjectNotFoundException;
import com.gabrielqt.gtpay.mapper.MerchantMapper;
import com.gabrielqt.gtpay.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MerchantService{

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    public void createForUser(User user){
        Merchant merchant = Merchant.builder()
                .baseUrl("")
                .user(user)
                .build();
        merchantRepository.save(merchant);
    }

    @Transactional
    public MerchantResponse updateBaseUrl(String baseUrl, Long merchantId){
        Merchant merchant = this.findById(merchantId);
        merchant.setBaseUrl(baseUrl); // dirty checking, its not necessary call .save()
        return merchantMapper.toMerchantResponse(merchant);
    }

    public Merchant findById(Long merchantId){
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new  ObjectNotFoundException("Merchant not Found"));
    }

    public Merchant findByUser(User user){
        return merchantRepository.findByUser(user)
                .orElseThrow(() -> new  ObjectNotFoundException("Merchant not Found"));
    }
}
