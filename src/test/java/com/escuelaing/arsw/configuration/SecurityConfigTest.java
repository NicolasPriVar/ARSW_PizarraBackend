package com.escuelaing.arsw.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testCorsConfigurationSourceIsNotNull() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source, "corsConfigurationSource() no debe retornar null");
    }

    @Test
    void testSecurityFilterChainBuildsSuccessfully() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        assertDoesNotThrow(() -> {
            SecurityFilterChain chain = securityConfig.securityFilterChain(http);
            assertNotNull(chain);
        });
    }
}
