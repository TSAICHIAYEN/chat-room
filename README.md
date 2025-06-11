# chat_room POC

## 後端 (Backend)
- Spring Boot 3.4.5
- Spring WebSocket
- Spring for Apache Kafka
- Kafka 3.8+（本地啟用於 localhost:9092）
- SockJS + STOMP（WebSocket 傳輸協定）
- Logback（客製化日誌紀錄）
- Java 17+

## 前端 (Frontend)
- HTML / JavaScript / Bootstrap
- SockJS + STOMP（用於與 WebSocket 後端溝通）

## 架構 (Architecture)
- WebSocket + Kafka 整合
