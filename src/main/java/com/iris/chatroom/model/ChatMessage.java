package com.iris.chatroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    public enum UserStatus {
        JOIN,
        LEAVE
    }
    public enum Type {
        MESSAGE,
        USER_STATUS
    }

    private UserStatus userStatus;
    private Type type;
    private String room;
    private String sender;
    private String content;
    private String timestamp;
}
