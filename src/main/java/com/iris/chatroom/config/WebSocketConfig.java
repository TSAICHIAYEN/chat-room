package com.iris.chatroom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 註冊 STOMP WebSocket 的端點（Endpoint），供前端用戶端連線使用。
     * <p>
     * 1. 建立 WebSocket 連線的入口點 "/ws"，前端要連線 WebSocket 時會以這個路徑作為起點。
     * 2. 設定允許的跨來源請求（CORS）來源，這裡使用萬用字元 "*" 表示允許所有來源（開發環境常見）。
     *    - 若上線到正式環境，建議使用 .setAllowedOrigins(...) 限定特定來源以加強安全性。
     * 3. 加入 SockJS 支援，讓瀏覽器在不支援原生 WebSocket 時可自動退回使用長輪詢等其他方式模擬。
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 註冊前端連接的 WebSocket 端點為 "/ws"
                .setAllowedOriginPatterns("*") // 允許所有網域的瀏覽器連線（開發用），正式環境請改用 setAllowedOrigins(...)
                //.setAllowedOrigins("http://localhost:9092", "https://example.com") // 指定允許的網域（註解範例）
                .withSockJS(); // 啟用 SockJS，兼容舊瀏覽器或無法建立 WebSocket 的環境
    }


    /**
     * 提供 WebSocket 用的心跳排程器（TaskScheduler）。
     * <p>
     * WebSocket 中的 STOMP 心跳（heartbeat）機制可以偵測連線是否仍存活。
     * 若在 enableSimpleBroker() 中設定了 setHeartbeatValue(...)，
     * 就需要提供一個 TaskScheduler 給 Spring 來執行這些心跳任務。
     * <p>
     * 為了避免跟 Spring 預設的內建 Bean（如 messageBrokerTaskScheduler）衝突，
     * 我們改為自定義一個名稱：myMessageBrokerTaskScheduler
     * <p>
     * 然後在 WebSocket 設定中手動註冊進去：
     * .setTaskScheduler(myMessageBrokerTaskScheduler())
     */
    @Bean
    public ThreadPoolTaskScheduler myMessageBrokerTaskScheduler() {
        // 建立一個 Spring 的排程器（基於 ThreadPool）
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        // 設定排程器的執行緒池大小，心跳需求不高，用 1 即可
        scheduler.setPoolSize(1);

        // 設定執行緒的命名前綴，有助於除錯觀察
        scheduler.setThreadNamePrefix("wss-heartbeat-thread-");

        // 初始化這個排程器
        scheduler.initialize();

        // 回傳排程器 Bean，Spring 會將其註冊進容器
        return scheduler;
    }


    /**
     * 設定 STOMP 訊息代理（Message Broker）的相關參數。
     * <p>
     * 1. 啟用內建的簡易訊息代理（Simple Broker）並指定前綴路徑，例如 "/topic"，用來接收來自後端廣播的訊息。
     * 2. 設定心跳（heartbeat）間隔為每 4 秒發送一次與接收一次，用來偵測連線是否斷線。
     * 3. 設定任務排程器（TaskScheduler）來執行這些心跳檢查的任務（非同步執行，避免主執行緒阻塞）。
     * 4. 設定前端傳送訊息給後端的路徑前綴為 "/app"，代表所有送往 Controller 的目標必須以這個開頭。
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic") // 設定後端廣播給前端的路徑前綴，例如 /topic/message
                .setHeartbeatValue(new long[]{4000, 4000}) // 設定 STOMP 心跳：client 每 4 秒發送一次，server 每 4 秒回應一次
                .setTaskScheduler(myMessageBrokerTaskScheduler()); // 指定負責心跳執行排程的 scheduler（避免卡住）

        config.setApplicationDestinationPrefixes("/app"); // 前端送出 STOMP 訊息的目的地 prefix（送給 Controller @MessageMapping）
    }


}



