package com.iris.chatroom.websocket;

import com.iris.chatroom.model.ChatMessage;
import com.iris.chatroom.service.producer.Sender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final Sender sender;

    private final Map<String, String> sessionIdToUsername = new ConcurrentHashMap<>();
    private final Map<String, String> sessionIdToRoom = new ConcurrentHashMap<>();

    /**
     * 處理 WebSocket 使用者意外斷線（例如關閉瀏覽器或網路中斷）。
     * 根據斷線的 sessionId 取得對應的使用者與聊天室資訊，
     * 並透過 Kafka 發送一則「使用者離開」訊息，由 Kafka consumer 廣播。
     *
     * @param event Spring 發出的 SessionDisconnectEvent 事件，包含斷線 session 的相關資訊
     */
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // 根據 sessionId 取得並移除 username 與 room 對應資訊
        String username = sessionIdToUsername.remove(sessionId);
        String room = sessionIdToRoom.remove(sessionId);

        if (username != null && room != null) {
            ChatMessage leaveMsg = new ChatMessage();
            leaveMsg.setSender(username);
            leaveMsg.setRoom(room);
            leaveMsg.setType(ChatMessage.Type.USER_STATUS);
            leaveMsg.setUserStatus(ChatMessage.UserStatus.LEAVE);
            leaveMsg.setTimestamp(Instant.now().toString());

            // 將離開訊息送至 Kafka，由 consumer 廣播至訂閱的 WebSocket 用戶
            sender.send(room, leaveMsg);
        }
    }

    /**
     * 註冊使用者加入聊天室的 session 資訊。
     * 當使用者透過 WebSocket 加入聊天室時，記住其 sessionId、username 與房間名稱，
     * 用於後續斷線判斷與訊息處理。
     *
     * @param sessionId WebSocket 的 session ID
     * @param username 加入聊天室的使用者名稱
     * @param room 使用者加入的聊天室名稱
     */
    public void registerSession(String sessionId, String username, String room) {
        sessionIdToUsername.put(sessionId, username);
        sessionIdToRoom.put(sessionId, room);
    }

}



