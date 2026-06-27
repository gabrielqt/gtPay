package com.gabrielqt.gtpay.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EMAIL_QUEUE = "email-queue";
    public static final String WEBHOOK_QUEUE = "webhook-queue";
    public static final String CHARGE_EXCHANGE = "charge-exchange";

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }
    @Bean
    public Queue webhookQueue() {
        return new Queue(WEBHOOK_QUEUE, true);
    }

    @Bean
    public FanoutExchange chargeExchange() {
        return new FanoutExchange(CHARGE_EXCHANGE);
    }


    @Bean
    public Binding webhookBinding(Queue webhookQueue, FanoutExchange chargeExchange) {
        return BindingBuilder.bind(webhookQueue).to(chargeExchange);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, FanoutExchange chargeExchange) {
        return BindingBuilder.bind(emailQueue).to(chargeExchange);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
