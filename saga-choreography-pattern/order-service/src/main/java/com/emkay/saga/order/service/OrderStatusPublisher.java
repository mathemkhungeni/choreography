package com.emkay.saga.order.service;

import com.emkay.saga.commons.dto.OrderRequestDto;
import com.emkay.saga.commons.event.OrderEvent;
import com.emkay.saga.commons.event.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusPublisher {

    private static final String ORDER_EVENT_TOPIC = "order-event";

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        OrderEvent orderEvent = new OrderEvent(orderRequestDto, orderStatus);
        kafkaTemplate.send(ORDER_EVENT_TOPIC, orderEvent);
    }
}
