package com.internship.auditservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI auditApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Audit Service API")
                        .description("""
                                REST API for accessing governance audit records.

                                Features:
                                - Retrieve policy audit history
                                - Track governance actions
                                - View who performed each action
                                - View when each action occurred
                                """)
                        .version("1.0")
                        .contact(new Contact()
                                .name("Tilahun Tareke")
                                .email("tilahuntareke8@gmail.com")));
    }
}