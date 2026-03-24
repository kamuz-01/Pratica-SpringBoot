package org.Pratica_SpringBoot.Config.ConfigSwagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

        @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API REST Projeto Spring Boot e Git e relacionamentos OneToMany e ManyToOne")
                        .version("1.0.0")
                        .description("API REST sobre um mini-sistema de gestão escolar, desenvolvida como parte de um projeto de prática com Spring Boot, Git e GitHub. A API inclui funcionalidades para gerenciar estudantes, cursos e matrículas, utilizando relacionamentos OneToMany e ManyToOne. O projeto é hospedado no GitHub, onde o código-fonte está disponível para consulta e colaboração."));
    }
}
