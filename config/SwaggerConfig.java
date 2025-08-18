package com.ninehub.authentication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Auth Service API")
                        .description("Authentication API with user management, roles, permissions, JWT, refresh token and OTP.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Beautero Kenne")
                                .email("beauterosaha@gmail.com")
                                .url("https://github.com/Beautero-ks/spring-auth-service")
                        ).license(new License().name("Apache 2.0").url("https://springdoc.org"))
                );
    }
}
