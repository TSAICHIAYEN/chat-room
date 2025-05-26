package com.iris.chatroom.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSession {
    private String username;
    private String room;
}
