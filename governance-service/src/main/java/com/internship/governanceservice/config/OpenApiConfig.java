package com.internship.governanceservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI governanceApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Governance Policy Management API")
                        .description("""
                                REST API for managing governance policies.
                                
                                Features:
                                - Create Policy
                                - Get Policies
                                - Submit Policy
                                - Approve Policy
                                - Reject Policy
                                
                                Events are published to Apache Kafka and consumed by the Audit Service.
                                """)
                        .version("1.0")
                        .contact(new Contact()
                                .name("Tilahun Tareke")
                                .email("tilahuntareke8@gmail.com")));
    }

}