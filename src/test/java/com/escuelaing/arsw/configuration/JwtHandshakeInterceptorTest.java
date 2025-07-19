package com.escuelaing.arsw.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para JwtHandshakeInterceptor.
 * Se verifica el comportamiento del interceptor ante diferentes escenarios
 * de tokens en la URI de conexión.
 */
@ExtendWith(MockitoExtension.class)
class JwtHandshakeInterceptorTest {

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private WebSocketHandler wsHandler;

    @InjectMocks
    private JwtHandshakeInterceptor interceptor;

    private Map<String, Object> attributes;

    @BeforeEach
    void setUp() {
        attributes = new HashMap<>();
    }

    /**
     * Prueba el caso exitoso donde se provee un token JWT válido en la query.
     * Debería permitir el handshake y añadir el email del usuario a los atributos.
     */
    @Test
    void givenValidToken_whenBeforeHandshake_thenReturnsTrueAndAddsEmailToAttributes() throws Exception {
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3RAdXNlci5jb20ifQ.signature";
        when(request.getURI()).thenReturn(new URI("ws://localhost/ws?token=" + validToken));
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);
        assertTrue(result, "El handshake debería ser permitido con un token válido.");
        assertEquals("test@user.com", attributes.get("userEmail"), "El email del usuario debería ser extraído y guardado en los atributos.");
    }

    /**
     * Prueba el caso donde el token en la query es inválido o malformado.
     * Debería denegar el handshake.
     */
    @Test
    void givenInvalidToken_whenBeforeHandshake_thenReturnsFalse() throws Exception {
        when(request.getURI()).thenReturn(new URI("ws://localhost/ws?token=esto.no.es.un.token"));
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);
        assertFalse(result, "El handshake debería ser denegado con un token inválido.");
        assertTrue(attributes.isEmpty(), "No se deberían añadir atributos si el token es inválido.");
    }

    /**
     * Prueba el caso donde no se provee el parámetro 'token' en la query.
     * Debería denegar el handshake.
     */
    @Test
    void givenNoTokenInQuery_whenBeforeHandshake_thenReturnsFalse() throws Exception {
        when(request.getURI()).thenReturn(new URI("ws://localhost/ws?param=value"));
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);
        assertFalse(result, "El handshake debería ser denegado si no hay token.");
    }

    /**
     * Prueba el caso donde la URI no tiene query string.
     * Debería denegar el handshake.
     */
    @Test
    void givenNoQueryInUri_whenBeforeHandshake_thenReturnsFalse() throws Exception {
        when(request.getURI()).thenReturn(new URI("ws://localhost/ws"));
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);
        assertFalse(result, "El handshake debería ser denegado si no hay query string.");
    }

    /**
     * El método afterHandshake es un no-op (no hace nada), pero lo probamos
     * para asegurar que no lanza excepciones inesperadas.
     */
    @Test
    void whenAfterHandshake_thenNoExceptionIsThrown() {
        assertDoesNotThrow(() -> interceptor.afterHandshake(request, response, wsHandler, null));
    }
}