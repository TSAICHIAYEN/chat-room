package com.iris.chatroom.service.consumer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;


@Getter
@Slf4j
@Component
public class Receiver {
    private final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "${kafka.topic.test_iris}")
    public void receive(String payload) {
        log.info("Received payload: {}", payload);
        latch.countDown();
    }

    @KafkaListener(topics = "${kafka.topic.my-topic}")
    public void receiveMyTopic(String payload) {
        log.info("Received payload: {}", payload);
        latch.countDown();
    }
}
