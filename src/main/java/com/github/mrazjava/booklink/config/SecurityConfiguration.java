package com.github.mrazjava.booklink.config;

import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Configuration of Spring security.
 *
 * @since 0.1.0
 */
@Configuration
@ConfigurationProperties(prefix = "booklink.security")
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements AllowedCorsEntries {

    @Inject
    private Logger log;

    private List<String> corsAllowOrigins;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * Restrict access to only known clients such as the frontend.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        log.info("enabling {} CORS origins:\n{}", corsAllowOrigins.size(), corsAllowOrigins);

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsAllowOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "PATCH", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/rest/**", configuration);
        return source;
    }

    @Override
    public List<String> getAllowedEntries() {
        return Collections.unmodifiableList(corsAllowOrigins);
    }

    void setCorsAllowOrigins(List<String> corsAllowOrigins) {
        this.corsAllowOrigins = corsAllowOrigins;
    }
}
