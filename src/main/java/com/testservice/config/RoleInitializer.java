package com.testservice.config;

import com.testservice.model.Role;
import com.testservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        // Create roles if they don't exist
        List<Role.ERole> roles = Arrays.asList(
                Role.ERole.ROLE_STUDENT,
                Role.ERole.ROLE_TEACHER,
                Role.ERole.ROLE_ADMIN);

        for (Role.ERole roleName : roles) {
            if (!roleRepository.findByName(roleName).isPresent()) {
                Role role = new Role(roleName);
                roleRepository.save(role);
                System.out.println("Role created: " + roleName);
            }
        }
    }
} 