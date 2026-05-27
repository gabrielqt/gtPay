package com.gabrielqt.gtpay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RouteConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

        configurer.addPathPrefix(
                "/api/v1",
                clazz -> clazz.getPackageName().contains(".api.")
        );

        configurer.addPathPrefix(
                "/webhook",
                clazz -> clazz.getPackageName().contains(".webhook.")
        );
    }
}