package com.github.mrazjava.booklink.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class LoggingConfiguration {

    @Bean
    @Scope("prototype")
    @Primary
    public Logger logger(InjectionPoint injectionPoint) {
        // Constructor injection
        if (injectionPoint != null && injectionPoint.getMethodParameter() != null && injectionPoint.getMethodParameter().getContainingClass() != null) {
            return LoggerFactory.getLogger(injectionPoint.getMethodParameter().getContainingClass());
        }
        // Field injection
        else if (injectionPoint != null && injectionPoint.getField() != null && injectionPoint.getField().getDeclaringClass() != null) {
            return LoggerFactory.getLogger(injectionPoint.getField().getDeclaringClass());
        }
        // Fallback
        else {
            return LoggerFactory.getLogger("BOOKLINK");
        }
    }
}
