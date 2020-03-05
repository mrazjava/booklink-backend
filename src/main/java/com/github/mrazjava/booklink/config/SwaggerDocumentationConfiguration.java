package com.github.mrazjava.booklink.config;

import com.github.mrazjava.booklink.BooklinkApp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @since 0.1.0
 */
@Configuration
public class SwaggerDocumentationConfiguration {

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
        Docket d = new Docket(DocumentationType.SWAGGER_2)
                .groupName("booklink-" + version)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BooklinkApp.class.getPackageName()))
                .build()
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.OffsetDateTime.class, java.util.Date.class)
                .apiInfo(generateInternalApiInfo(version, "booklink-backend", "booklink services (business logic)"));

        if (StringUtils.isNotBlank(swaggerhost)) {
            d.host(swaggerhost);
        }
        return d;
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
