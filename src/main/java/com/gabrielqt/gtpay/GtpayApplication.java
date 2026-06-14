package com.gabrielqt.gtpay;

import com.gabrielqt.gtpay.service.helper.CepFinder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class GtpayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GtpayApplication.class, args);
	}

//	@Bean
//	CommandLineRunner test(CepFinder cepFinder) {
//		return args -> {
//			System.out.println(cepFinder.findCityByCep("00001000"));
//		};
//	}

}
