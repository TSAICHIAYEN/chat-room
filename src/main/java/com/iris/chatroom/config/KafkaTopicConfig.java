package com.iris.chatroom.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicConfig {
    private List<String> chatRooms;  // chat_rooms 直接綁定為 List
    private Map<String, String> topics;  // 其他的 topics 綁定為 Map

    /**
     * 自動建立聊天室topic
     * @param config
     * @return
     */
    @Bean
    public List<NewTopic> chatTopics(KafkaTopicConfig config) {
        return config.getChatRooms().stream()
                .map(room -> new NewTopic(room, 1, (short) 1))
                .collect(Collectors.toList());
    }

}
