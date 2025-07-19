package com.escuelaing.arsw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CanvasControllerTest {
    private CanvasController canvasController;
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    public void setUp() {
        messagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
        canvasController = new CanvasController();

        var field = Arrays.stream(CanvasController.class.getDeclaredFields())
                .filter(f -> f.getType().equals(SimpMessagingTemplate.class))
                .findFirst()
                .orElseThrow();
        field.setAccessible(true);
        try {
            field.set(canvasController, messagingTemplate);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBroadcastDrawBatch() {
        List<String> messages = Arrays.asList("line1", "line2", "line3");
        canvasController.broadcastDrawBatch(messages);
        for (String message : messages) {
            verify(messagingTemplate, times(1)).convertAndSend("/topic/board", message);
        }

        verifyNoMoreInteractions(messagingTemplate);
    }
    @Test
    public void testBroadcastDraw() {
        CanvasController controller = new CanvasController();
        String input = "draw-data";
        String result = controller.broadcastDraw(input);
        assertEquals(input, result);
    }

    @Test
    public void testBroadcastChat() {
        CanvasController controller = new CanvasController();
        String input = "Hello from chat";
        String result = controller.broadcastChat(input);
        assertEquals(input, result);
    }

}
