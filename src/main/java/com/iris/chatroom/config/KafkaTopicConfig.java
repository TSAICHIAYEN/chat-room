package com.iris.chatroom.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicConfig {
    private List<String> chatRooms;  // chat_rooms 直接綁定為 List
    private Map<String, String> topics;  // 其他的 topics 綁定為 Map

}
