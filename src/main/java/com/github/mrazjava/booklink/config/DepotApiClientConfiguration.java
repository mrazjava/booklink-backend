package com.github.mrazjava.booklink.config;

import com.github.mrazjava.booklink.rest.depot.ApiClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author AZ (mrazjava)
 * @since 0.3.0
 */
@Configuration
public class DepotApiClientConfiguration {

    @Inject
    private Logger log;

    @Value("${booklink.depot.url}")
    private String depotBaseUrl;

    @Autowired
    @Qualifier("com.github.mrazjava.booklink.rest.depot.ApiClient")
    private ApiClient apiClient;

    @PostConstruct
    void initialize() {
        log.info(".............................................................");
        log.info("setting base path for {} (depot) to: {}", ApiClient.class.getSimpleName(), depotBaseUrl);
        log.info(".............................................................");
        apiClient.setBasePath(depotBaseUrl);
    }
}
