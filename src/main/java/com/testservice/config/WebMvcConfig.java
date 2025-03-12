package com.testservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve our custom Swagger UI HTML file
        registry.addResourceHandler("/swagger-ui-custom.html")
                .addResourceLocations("classpath:/static/swagger-ui-custom.html");
        
        // Serve Swagger UI WebJar properly
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false);
        
        // Optional: Add resource handler for v3 API docs if needed
        registry.addResourceHandler("/v3/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/v3/api-docs/");
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map /swagger URL to our custom Swagger UI page
        registry.addViewController("/swagger")
                .setViewName("forward:/swagger-ui-custom.html");
    }
} 