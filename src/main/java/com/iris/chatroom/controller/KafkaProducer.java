package com.iris.chatroom.controller;

import com.iris.chatroom.dto.Book;
import com.iris.chatroom.service.producer.Sender;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class KafkaProducer {

    private final Sender sender;

    @RequestMapping(value = "/sender.action", method = RequestMethod.POST)
    public void execute(HttpServletResponse response, @RequestParam("name") String name) throws IOException {
        Book book = new Book();
        book.setId(1);
        book.setAuthor("lululala");
        book.setTitle(name);
        sender.send("my-topic", book);
        sender.send("test_iris", book);
        response.setContentType("text/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("success");
        response.getWriter().flush();
        response.getWriter().close();
    }
}
