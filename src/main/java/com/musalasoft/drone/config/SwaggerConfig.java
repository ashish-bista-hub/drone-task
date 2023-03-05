package com.musalasoft.drone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @author ashish.bista
 * @since 03/03/2023
 */
@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.musalasoft.drone.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Musala Soft Drone Task Rest API", //title
                "This rest API will support a major new technology that is destined to be a disruptive force in the " +
                        "field of transportation: the drone.", //description
                "Version 1.0", //version
                "Terms of service", //terms of service URL
                new Contact("Musala Soft", "https://www.musala.com/", "info@musala.com"),
                "© 2023 Musala Soft", "Drone API license URL", Collections.emptyList()); // contact info
    }
}
