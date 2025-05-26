package com.iris.chatroom.service.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Component
public class Sender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Object message) {
        log.info("send message: '{}' to topic: '{}'", message, topic);

        kafkaTemplate.send(topic, message);
    }
}
