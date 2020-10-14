package com.github.mrazjava.booklink.config;

import com.github.mrazjava.booklink.rest.depot.ApiClient;
import com.github.mrazjava.booklink.rest.depot.DepotAuthorApi;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * @author AZ (mrazjava)
 * @since 0.3.0
 */
@Configuration
public class DepotConfiguration {

    public static final String DEPOT_API = "OpenLibraryDepotApiClient";

    @Inject
    private Logger log;

    @Bean(DEPOT_API)
    ApiClient produceDepotApiClient(@Value("${booklink.depot.url}") String depotBaseUrl) {
        log.debug("producing {}", ApiClient.class.getCanonicalName());
        ApiClient depotClient = new ApiClient();
        depotClient.setBasePath(depotBaseUrl);
        return depotClient;
    }

    @Bean
    DepotAuthorApi produceDepotAuthorApi(@Qualifier(DEPOT_API) ApiClient apiClient) {
        log.debug("producing {}", DepotAuthorApi.class.getCanonicalName());
        return new DepotAuthorApi(apiClient);
    }
}
