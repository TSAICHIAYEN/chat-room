package com.iris.chatroom.controller;

import com.iris.chatroom.model.ChatMessage;
import com.iris.chatroom.model.TypingNotice;
import com.iris.chatroom.service.producer.Sender;
import com.iris.chatroom.websocket.WebSocketEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final Sender sender;
    private final WebSocketEventListener webSocketEventListener;

    /**
     * 處理使用者透過 STOMP WebSocket 發送聊天訊息。
     * 將訊息送至 Kafka，由 Kafka consumer 統一轉發給聊天室的訂閱者。
     *
     * @param message 使用者傳送的聊天訊息物件
     * @return 回應 "Message sent!" 給發送端（通常 WebSocket 客戶端不會處理這個 ResponseEntity）
     */
    @MessageMapping("/chat.sendMessage")
//    @PostMapping("/chat.sendMessage") // 如果想用 Postman 測試，可以改用此註解，但與 @MessageMapping 不能同時存在
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessage message) {
        System.out.println("message:" + message.toString());
        message.setType(ChatMessage.Type.MESSAGE);

        // 將訊息送到 Kafka，後續由 Consumer 負責轉發給 WebSocket 訂閱者
        sender.send(message.getRoom(), message);

        log.info("Message sent to room {}: {}", message.getRoom(), message);
        return ResponseEntity.ok("Message sent!");
    }

    /**
     * 處理使用者加入聊天室的 STOMP 請求。
     * 儲存使用者資訊至 WebSocket session 中，並將加入訊息送往 Kafka。
     *
     * @param message        包含發送者資訊的 ChatMessage 物件
     * @param headerAccessor 用於取得與設定 WebSocket session 屬性
     */
    @MessageMapping("/chat.join")
    public void joinRoom(ChatMessage message,
                         SimpMessageHeaderAccessor headerAccessor) {

        message.setType(ChatMessage.Type.USER_STATUS);
        message.setUserStatus(ChatMessage.UserStatus.JOIN);
        message.setTimestamp(Instant.now().toString());

        // 儲存使用者名稱與房間資訊到 WebSocket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", message.getSender());
        headerAccessor.getSessionAttributes().put("room", message.getRoom());

        // 註冊 sessionId 對應的使用者與聊天室資訊
        webSocketEventListener.registerSession(headerAccessor.getSessionId(), message.getSender(), message.getRoom());

        // 將加入聊天室訊息送至 Kafka，由 consumer 廣播給訂閱者
        sender.send(message.getRoom(), message);
    }

    /**
     * 處理使用者透過 REST API 主動離開聊天室。
     * 將離開訊息送往 Kafka，由 Kafka consumer 統一轉發給聊天室成員。
     *
     * @param message 離開聊天室的使用者訊息（包含房間與發送者資訊）
     * @return HTTP 200 OK 表示處理成功
     */
    @PostMapping("/leave")
    public ResponseEntity<Void> leave(@RequestBody ChatMessage message) {
        message.setType(ChatMessage.Type.USER_STATUS);
        message.setUserStatus(ChatMessage.UserStatus.LEAVE);
        message.setTimestamp(Instant.now().toString());

        // 將離開聊天室的訊息送至 Kafka，由 consumer 廣播
        sender.send(message.getRoom(), message);

        return ResponseEntity.ok().build();
    }

    /**
     * 實現正在輸入...功能
     */
    @MessageMapping("/chat.typing")
    @SendTo("/topic/typing")
    public TypingNotice typing(TypingNotice notice) {
        return notice; // 將 sender 廣播出去
    }
}
