package com.testservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Öğrenci Test Servisi API")
                        .version("1.0")
                        .description("Öğrencilerin test çözebileceği bir servis API'si")
                        .contact(new Contact()
                                .name("Test Service Team")
                                .email("testservice@example.com")));
    }
}