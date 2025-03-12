package com.testservice.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class SecurityPropertiesTest {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Test
    void testJwtSecretIsConfigured() {
        assertNotNull(jwtSecret);
        assertEquals("testSecretKeyForJwtUtilsTests123456789012345678901234567890", jwtSecret);
    }

    @Test
    void testJwtExpirationIsConfigured() {
        assertEquals(60000, jwtExpirationMs); // 1 minute
    }
}