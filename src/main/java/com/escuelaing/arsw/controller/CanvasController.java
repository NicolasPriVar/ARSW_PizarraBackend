package com.escuelaing.arsw.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class CanvasController {

    @MessageMapping("/draw")
    @SendTo("/topic/board")
    public String broadcastDraw(String message) {
        return message;
    }
}
