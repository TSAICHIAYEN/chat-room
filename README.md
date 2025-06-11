# chat_room POC

## 快速啟動
### 前置需求
- 安裝 Docker
- 安裝 Docker Compose
- Kafka 必須運作在 localhost:9092
### 啟動 docker-compose 指令
- docker-compose up -d
### 啟動 Spring boot 專案
- ./mvnw spring-boot:run

## 後端 (Backend)
- Spring Boot 3.4.5
- Spring WebSocket
- Spring for Apache Kafka
- Kafka 3.8+（本地啟用於 localhost:9092）
- SockJS + STOMP（WebSocket 傳輸協定）
- Logback（客製化日誌紀錄）
- Java 17+
- Maven

## 前端 (Frontend)
- HTML / JavaScript / Bootstrap
- SockJS + STOMP（用於與 WebSocket 後端溝通）

## 開發工具
- IntelliJ IDEA
- Docker
- Git + GitHub
- Jenkins（未來規劃）

## TODO
### 單元測試（Unit Test）
- 使用 JUnit 5（spring-boot-starter-test）
- Kafka 消費者/生產者邏輯測試
- WebSocket 控制器測試
- Mockito 模擬依賴元件（如 KafkaTemplate）

### 自動化測試（CI/CD）
- Jenkins 建立 Pipeline 自動化流程
- Push/PR 觸發測試
- 測試通過後建構與部署
