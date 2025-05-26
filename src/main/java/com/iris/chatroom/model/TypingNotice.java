package com.iris.chatroom.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypingNotice {

    private String sender;

    public TypingNotice() {}

    public TypingNotice(String sender) {
        this.sender = sender;
    }


}
