package com.github.mrazjava.booklink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @since 0.1.0
 */
@SpringBootApplication(
    scanBasePackages = {"com.github.mrazjava.booklink"}
)
@EnableJpaRepositories(
    basePackages = "com.github.mrazjava.booklink.persistence.repository"
)
public class BooklinkApp {

    private static final Logger log = LoggerFactory.getLogger(BooklinkApp.class);

    public static void main(String[] args) {

        log.info("initializing Booklink backend ...");

        new SpringApplicationBuilder()
                .sources(BooklinkApp.class)
                .run(args);
    }
}
