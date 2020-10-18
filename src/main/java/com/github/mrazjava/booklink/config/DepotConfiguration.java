package com.github.mrazjava.booklink.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.github.mrazjava.booklink.rest.depot.ApiClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author AZ (mrazjava)
 * @since 0.3.0
 */
@Configuration
public class DepotConfiguration {

    @Value("${booklink.depot.url}")
    private String depotBaseUrl;

    @Inject
    private Logger log;

    @Autowired
    @Qualifier("com.github.mrazjava.booklink.rest.depot.ApiClient")
    private ApiClient apiClient;

    @PostConstruct
    void initialize() {
        log.info("............................................................");
        log.info("setting base path for {} to: {}", ApiClient.class.getSimpleName(), depotBaseUrl);
        log.info("............................................................");
        apiClient.setBasePath(depotBaseUrl);
    }

    @Bean
    public RestTemplate provideRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .messageConverters(createMappingJackson2HttpMessageConverter())
                .build();
    }

    private ObjectMapper createObjectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(
                new JavaTimeModule().addDeserializer(
                        LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME
                )
            )
        );

        return mapper;
    }

    private MappingJackson2HttpMessageConverter createMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(createObjectMapper());
        return converter;
    }
}
