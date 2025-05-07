package com.iris.chatroom.controller;

import com.example.myfirstspringboot.dto.ChatMessage;
import com.example.myfirstspringboot.service.producer.Sender;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final Sender sender;

    @MessageMapping("/chat.sendMessage") // ✅ STOMP WebSocket 端點
//    @PostMapping("/{room}/send") // @MessageMapping 跟 @PostMapping 只能同時存在一個 如果要用postman測試的話可以先把這個打開
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessage message) throws JsonProcessingException {
        System.out.println("message:" + message.toString());
        sender.send(message.getRoom(), message);
        log.info("Message sent to room {}: {}", message.getRoom(), message);
        return ResponseEntity.ok("Message sent!");
    }
}
