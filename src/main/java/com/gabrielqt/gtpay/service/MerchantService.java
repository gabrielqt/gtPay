package com.gabrielqt.gtpay.service;


import com.gabrielqt.gtpay.dto.response.BaseUrlResponse;
import com.gabrielqt.gtpay.dto.response.MerchantResponse;
import com.gabrielqt.gtpay.entity.Merchant;
import com.gabrielqt.gtpay.entity.User;
import com.gabrielqt.gtpay.exception.ObjectNotFoundException;
import com.gabrielqt.gtpay.mapper.MerchantMapper;
import com.gabrielqt.gtpay.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MerchantService{

    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    public void createForUser(User user){
        Merchant merchant = Merchant.builder()
                .user(user)
                .build();
        merchantRepository.save(merchant);
    }

    @Transactional
    public BaseUrlResponse updateBaseUrl(String baseUrl, User user){
        Merchant merchant = this.findByUser(user);
        merchant.setBaseUrl(baseUrl); // dirty checking, its not necessary call .save()
        return new BaseUrlResponse(merchant.getBaseUrl());
    }

    public MerchantResponse findById(Long merchantId){
        Merchant merchant =  merchantRepository.findById(merchantId)
                .orElseThrow(() -> new  ObjectNotFoundException("Merchant not Found"));
        return merchantMapper.toMerchantResponse(merchant);
    }

    public Merchant findByUser(User user){
        return merchantRepository.findByUser(user)
                .orElseThrow(() -> new  ObjectNotFoundException("Merchant not Found"));
    }

    public Page<MerchantResponse> findAll(Pageable pageable){
        return merchantRepository.findAll(pageable).map(merchantMapper::toMerchantResponse);
    }

}
