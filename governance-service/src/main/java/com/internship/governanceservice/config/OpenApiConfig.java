package com.internship.governanceservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Governance Policy Management API",
                version = "1.0.0",
                description = "REST API for managing governance policies with event-driven audit logging using Apache Kafka.",
                contact = @Contact(
                        name = "Tilahun Tareke",
                        email = "tilahuntarek8@gmail.com"
                ),
                license = @License(
                        name = "MIT License"
                )
        )
)
public class OpenApiConfig {
}