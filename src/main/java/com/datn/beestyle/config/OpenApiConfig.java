package com.datn.beestyle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Cấu Hình API Documents, Swagger Cho Spring Boot
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${openapi.service.title}") String title,
                           @Value("${openapi.service.version}") String version,
                           @Value("${openapi.service.description}") String description,
                           @Value("${openapi.service.serverUrl}") String serverUrl,
                           @Value("${openapi.service.serverName}") String serverName) {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .license(new License().name("API License").url("https://domain.vn/license")))
                .servers(List.of(new Server().url(serverUrl).description(serverName)));
//                .components(new Components()
//                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")))
//                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }

    @Bean
    public GroupedOpenApi groupedUser(@Value("${openapi.service.api-docs}") String groupApi) {
        return GroupedOpenApi.builder()
                .group(groupApi) // /v1/api-docs
                .packagesToScan("com.datn.beestyle.controller") // scan tất cả các bean controller nằm trong package
                .build();
    }
}
