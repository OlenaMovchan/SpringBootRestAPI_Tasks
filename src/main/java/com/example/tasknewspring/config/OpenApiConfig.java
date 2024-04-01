package com.example.tasknewspring.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        email = "elenawju@gmail.com"
                ),
                description = "Tasks information",
                title = "Tasks API",
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8082"
                ),
                @Server(
                        description = "AWS ENV",
                        url = "http://Springusers-env.eba-macic9ka.eu-central-1.elasticbeanstalk.com"
                )
        }
)
@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class OpenApiConfig {

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> operation
                .addParametersItem(new Parameter()
                        .in("header").required(false).description("Language selection")
                        .name("Accept-Language"));
    }
}
