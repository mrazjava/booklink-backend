package com.github.mrazjava.booklink.config;

import java.util.Collections;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.mrazjava.booklink.BooklinkApp;
import com.github.mrazjava.booklink.security.AccessTokenSecurityFilter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author AZ
 */
@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    public static final String HEADER_NOT_USED_MSG = "NOT USED - ignore";

    @Inject
    private Logger log;
    
    @Inject
    private Optional<BuildProperties> build;

    @Inject
    private Optional<GitProperties> git;

    @Value("${info.app.description}")
    private String appdesc;

    @Value("${swaggerhost:}")
    private String swaggerhost;

    @Bean
    public Docket internalAPI() {

        final String version = (build.isPresent()) ? build.get().getVersion() : "";
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("booklink-" + version)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BooklinkApp.class.getPackageName()))
                .build()
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
                .apiInfo(generateInternalApiInfo(version, "booklink-backend", "booklink services (business logic)"))
                .globalOperationParameters(
                        Collections.singletonList(new ParameterBuilder()
                                .name(AccessTokenSecurityFilter.AUTHORIZATION_HEADER_NAME)
                                .description("auth token is required for all secured endpoints (login to obtain a token)")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)
                                .build())
                );

        if (StringUtils.isNotBlank(swaggerhost)) {
            log.info(".............................................................");
            log.info("setting base path for {} (backend) to: {}", SwaggerConfiguration.class.getSimpleName(), swaggerhost);
            log.info(".............................................................");        	
            docket.host(swaggerhost);
        }
        return docket;
    }

    private ApiInfo generateInternalApiInfo(String version, String title, String desc) {
        String description = desc;
        if (build.isPresent() && git.isPresent()) {
            BuildProperties buildInfo = build.get();
            GitProperties gitInfo = git.get();
            version = buildInfo.getVersion() + "-" + gitInfo.getShortCommitId() + "-" + gitInfo.getBranch();
        }

        if (desc != null) {
            description = "<b>" + appdesc + "</b>" + " <br><br>" + desc;
        }

        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .license("")
                .licenseUrl("")
                .termsOfServiceUrl("")
                .version(version)
                .contact(new Contact("mrazjava", "https://github.com/mrazjava", ""))
                .build();
    }
}
