package com.escuelaing.arsw.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase WebSocketConfig.
 * Estas pruebas verifican que los endpoints STOMP y el message broker
 * se configuran correctamente sin levantar el contexto de Spring.
 */
@ExtendWith(MockitoExtension.class)
class WebSocketConfigTest {

    @Mock
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Mock
    private StompEndpointRegistry stompEndpointRegistry;

    @Mock
    private MessageBrokerRegistry messageBrokerRegistry;

    @InjectMocks
    private WebSocketConfig webSocketConfig;

    /**
     * Prueba que el método registerStompEndpoints configura el endpoint "/ws"
     * con el interceptor, los orígenes permitidos y el soporte para SockJS.
     */
    @Test
    void whenRegisterStompEndpoints_thenCorrectConfigurationIsApplied() {
        StompWebSocketEndpointRegistration endpointRegistration = mock(StompWebSocketEndpointRegistration.class);
        when(stompEndpointRegistry.addEndpoint("/ws")).thenReturn(endpointRegistration);
        when(endpointRegistration.addInterceptors(jwtHandshakeInterceptor)).thenReturn(endpointRegistration);
        when(endpointRegistration.setAllowedOriginPatterns("*")).thenReturn(endpointRegistration);
        webSocketConfig.registerStompEndpoints(stompEndpointRegistry);
        verify(stompEndpointRegistry).addEndpoint("/ws");
        verify(endpointRegistration).addInterceptors(jwtHandshakeInterceptor);
        verify(endpointRegistration).setAllowedOriginPatterns("*");
        verify(endpointRegistration).withSockJS();
    }

    /**
     * Prueba que el método configureMessageBroker habilita un simple broker
     * en "/topic" y establece el prefijo de la aplicación en "/app".
     */
    @Test
    void whenConfigureMessageBroker_thenBrokerAndPrefixAreSet() {
        webSocketConfig.configureMessageBroker(messageBrokerRegistry);
        verify(messageBrokerRegistry).enableSimpleBroker("/topic");
        verify(messageBrokerRegistry).setApplicationDestinationPrefixes("/app");
    }
}