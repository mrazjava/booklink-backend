package com.github.mrazjava.booklink.config;

import com.github.mrazjava.booklink.rest.depot.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author AZ (mrazjava)
 * @since 0.3.0
 */
@Configuration
public class DepotConfiguration {

    @Bean("OpenLibraryDepotApiClient")
    ApiClient generateDepotRestClient(@Value("${booklink.depot.url}") String depotBaseUrl) {
        ApiClient depotClient = new ApiClient();
        depotClient.setBasePath(depotBaseUrl);
        return depotClient;
    }
}
