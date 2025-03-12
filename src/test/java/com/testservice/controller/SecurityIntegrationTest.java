package com.testservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testservice.dto.request.LoginRequest;
import com.testservice.dto.request.SignupRequest;
import com.testservice.model.Role;
import com.testservice.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private CacheManager cacheManager;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        // Clear the cache before each test
        Objects.requireNonNull(cacheManager.getCache("studentsPage")).clear();
        Objects.requireNonNull(cacheManager.getCache("students")).clear();
        
        // Ensure roles exist
        if (!roleRepository.findByName(Role.ERole.ROLE_STUDENT).isPresent()) {
            roleRepository.save(new Role(Role.ERole.ROLE_STUDENT));
        }
        if (!roleRepository.findByName(Role.ERole.ROLE_TEACHER).isPresent()) {
            roleRepository.save(new Role(Role.ERole.ROLE_TEACHER));
        }
        if (!roleRepository.findByName(Role.ERole.ROLE_ADMIN).isPresent()) {
            roleRepository.save(new Role(Role.ERole.ROLE_ADMIN));
        }

        // Setup signup request
        signupRequest = new SignupRequest();
        signupRequest.setUsername("integrationTestUser");
        signupRequest.setEmail("integration@test.com");
        signupRequest.setPassword("password123");
        Set<String> roles = new HashSet<>();
        roles.add("student");
        signupRequest.setRoles(roles);

        // Setup login request
        loginRequest = new LoginRequest();
        loginRequest.setUsername("integrationTestUser");
        loginRequest.setPassword("password123");
    }

    @Test
    void testRegisterAndLogin() throws Exception {
        // Register a new user
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Login to get JWT token
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(response).get("token").asText();
        
        assertNotNull(token, "JWT token should not be null");
        
        // Access endpoint with JWT token
        mockMvc.perform(get("/api/students")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "STUDENT")
    void testEndpointAccess() throws Exception {
        // Test endpoint access with mock user
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }
} 