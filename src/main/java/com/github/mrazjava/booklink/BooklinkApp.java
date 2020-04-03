package com.github.mrazjava.booklink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @since 0.1.0
 */
@SpringBootApplication(scanBasePackages = {
        "com.github.mrazjava.booklink"
})
@EnableJpaRepositories(
        basePackages = "com.github.mrazjava.booklink.persistence.repository")
@EnableSwagger2
public class BooklinkApp {

    private static final Logger log = LoggerFactory.getLogger(BooklinkApp.class);

    public static void main(String[] args) {

//        log.info(".... [{}]-[{}]-[{}]",
//                System.getenv("AWS_ACCESS_KEY_ID"),
//                System.getenv("AWS_SECRET_ACCESS_KEY"),
//                System.getenv("AWS_REGION")
//        );

        new SpringApplicationBuilder()
                .sources(BooklinkApp.class)
                .run(args);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propsConfig
                = new PropertySourcesPlaceholderConfigurer();
        propsConfig.setLocation(new ClassPathResource("git.properties"));
        propsConfig.setIgnoreResourceNotFound(true);
        propsConfig.setIgnoreUnresolvablePlaceholders(true);
        return propsConfig;
    }
}
