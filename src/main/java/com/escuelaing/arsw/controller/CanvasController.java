package com.escuelaing.arsw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class CanvasController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/draw")
    @SendTo("/topic/board")
    public String broadcastDraw(String message) {
        return message;
    }

    @MessageMapping("/draw-batch")
    public void broadcastDrawBatch(List<String> messages) {
        for (String message : messages) {
            messagingTemplate.convertAndSend("/topic/board", message);
        }
    }
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public String broadcastChat(String message) {
        return message;
    }
}