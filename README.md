# chat_room POC

## 快速啟動
### 1. 前置需求
- 確認安裝 Docker
- 確認安裝 Docker Compose
- Kafka 必須運作在 localhost:9092
### 2. 啟動 docker-compose 指令
- docker-compose up -d
### 3. 啟動 Spring boot 專案
- ./mvnw spring-boot:run
### 4. 最後
- 在瀏覽器上輸入 http://localhost:9090/index_user1.html 即可使用聊天室


## 如果想用 k8s 管理專案就看這裡
### 1. 快速啟動做到步驟2

### 2. 已安裝 Docker Desktop 並開啟 Kubernetes 模式

### 3. 重新打包並建立 Docker image 指令
1. ./mvnw clean package -DskipTests (會把最新的程式打包成 target/*.jar)
2. docker build -t my-chat-app . (image 名稱 如果YAML 裡寫的是別的 這裡也要一致)

### 4. (如果有) 刪除原本的 Deployment
- kubectl delete -f k8s-deployment.yaml

### 5. 部署新的 Deployment + Service
- kubectl apply -f k8s-deployment.yaml

### 6. 檢查 k8s 是否成功建立指令
- kubectl get pods
- kubectl get svc

### 7. 最後
- 在瀏覽器上輸入 http://localhost:30090/index_user1.html 即可使用聊天室


## 後端 (Backend)
- Spring Boot 3.4.5
- Spring WebSocket
- Spring for Apache Kafka
- Kafka 3.8+（本地啟用於 localhost:9092）
- SockJS + STOMP（WebSocket 傳輸協定）
- Logback
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
