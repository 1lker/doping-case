package com.testservice.controller;

import com.testservice.dto.response.JwtResponse;
import com.testservice.model.Role;
import com.testservice.model.User;
import com.testservice.repository.RoleRepository;
import com.testservice.repository.UserRepository;
import com.testservice.security.jwt.JwtUtils;
import com.testservice.security.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dev")
@Profile({"dev", "test"}) // Only available in development and test profiles
@Tag(name = "Development Tools", description = "Utilities for development and testing - NOT FOR PRODUCTION")
public class DevToolsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123"; // Simple password for development only
    private static final String ADMIN_EMAIL = "admin@example.com";

    @GetMapping("/admin-auth")
    @Operation(
        summary = "Get Admin Authentication Token",
        description = "Creates an admin user if it doesn't exist and returns a JWT token - FOR DEVELOPMENT USE ONLY"
    )
    public ResponseEntity<JwtResponse> getAdminToken() {
        // Check if admin user exists, create if it doesn't
        if (!userRepository.existsByUsername(ADMIN_USERNAME)) {
            createAdminUser();
        }

        // Authenticate with the admin credentials
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(ADMIN_USERNAME, ADMIN_PASSWORD)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles,
            null // Admin user doesn't have an associated student ID
        ));
    }

    private void createAdminUser() {
        User user = new User();
        user.setUsername(ADMIN_USERNAME);
        user.setEmail(ADMIN_EMAIL);
        user.setPassword(encoder.encode(ADMIN_PASSWORD));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Set<Role> roles = new HashSet<>();
        
        // Add all roles to admin
        Role adminRole = roleRepository.findByName(Role.ERole.ROLE_ADMIN)
            .orElseThrow(() -> new RuntimeException("Error: Admin Role not found."));
        roles.add(adminRole);
        
        Role teacherRole = roleRepository.findByName(Role.ERole.ROLE_TEACHER)
            .orElseThrow(() -> new RuntimeException("Error: Teacher Role not found."));
        roles.add(teacherRole);
        
        Role studentRole = roleRepository.findByName(Role.ERole.ROLE_STUDENT)
            .orElseThrow(() -> new RuntimeException("Error: Student Role not found."));
        roles.add(studentRole);

        user.setRoles(roles);
        userRepository.save(user);
    }
} 