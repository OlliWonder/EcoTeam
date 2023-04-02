package com.sber.java13.ecoteam.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class OpenAPIConfig {
    
    //@Bean
    public OpenAPI libraryProject() {
        return new OpenAPI()
                .info(new Info()
                        .title("Личный кабинет участника EcoTeam")
                        .description("Сервис, который мотивирует людей к раздельному сбору мусора, а так же помогает" +
                                " компаниям-участникам оказывать услуги по вывозу мусора.")
                        .version("v0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                        .contact(new Contact().name("Olga A. Ivlieva")
                                .email("sunchess55@gmail.com")
                                .url(""))
                );
    }
}
