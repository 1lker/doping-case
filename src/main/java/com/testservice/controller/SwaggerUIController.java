package com.testservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerUIController {

    @GetMapping("/swagger")
    public String customSwaggerUI() {
        return "swagger-ui-custom.html";
    }
} 