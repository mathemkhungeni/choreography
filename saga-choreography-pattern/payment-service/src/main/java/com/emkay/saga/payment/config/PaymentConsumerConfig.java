package com.emkay.saga.payment.config;

import com.emkay.saga.commons.event.OrderEvent;
import com.emkay.saga.commons.event.OrderStatus;
import com.emkay.saga.commons.event.PaymentEvent;
import com.emkay.saga.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.function.Consumer;

@Configuration
public class PaymentConsumerConfig {

    private static final String PAYMENT_EVENT_TOPIC = "payment-event";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Bean
    public Consumer<OrderEvent> paymentProcessor() {
        return orderEvent -> {
            if (OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())) {
                PaymentEvent paymentEvent = paymentService.newOrderEvent(orderEvent);
                kafkaTemplate.send(PAYMENT_EVENT_TOPIC, paymentEvent);
            } else {
                paymentService.cancelOrderEvent(orderEvent);
            }
        };
    }
}
