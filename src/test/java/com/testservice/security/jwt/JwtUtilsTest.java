package com.testservice.security.jwt;

import com.testservice.security.service.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    private UserDetailsImpl userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        // Set up test JWT secret and expiration time
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKeyForJwtUtilsTests123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 60000); // 1 minute

        // Create test UserDetailsImpl object
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        userDetails = new UserDetailsImpl(1L, "testuser", "test@example.com", "password", authorities);
        
        // Create authentication object
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    void generateJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);
        
        // Token should not be null or empty
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getUserNameFromJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        
        // Username should match the one used to create the token
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void validateJwtToken() {
        String token = jwtUtils.generateJwtToken(authentication);
        
        // Valid token should be validated successfully
        assertTrue(jwtUtils.validateJwtToken(token));
        
        // Invalid token should fail validation
        assertFalse(jwtUtils.validateJwtToken("invalidToken"));
    }

    @Test
    void validateJwtToken_withMalformedToken() {
        // Malformed token should fail validation
        assertFalse(jwtUtils.validateJwtToken("malformedToken"));
    }

    @Test
    void validateJwtToken_withEmptyToken() {
        // Empty token should fail validation
        assertFalse(jwtUtils.validateJwtToken(""));
    }
} 