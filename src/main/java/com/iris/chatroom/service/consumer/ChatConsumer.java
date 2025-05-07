package com.iris.chatroom.service.consumer;

import com.example.myfirstspringboot.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatConsumer {

    private final SimpMessagingTemplate template;

//    @KafkaListener(topics = "#{T(java.util.stream.Collectors).toList().stream().filter(t -> chatRooms.contains(t)).collect(T(java.util.stream.Collectors).toList())}", groupId = "chat-group")

    @KafkaListener(topics = "#{kafkaTopicConfig.getChatRooms()}", groupId = "chat-group")
    public void listen(@Header("kafka_receivedTopic") String topic, ChatMessage message) {

        log.info("Received message in room {}: {}", topic, message.toString());
        // 轉發給 WebSocket 客戶端 前端有訂閱 /topic/chat_room_1 所以這邊要送回給websocket
        template.convertAndSend("/topic/" + topic, message);
    }


}
