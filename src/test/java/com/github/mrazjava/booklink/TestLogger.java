package com.github.mrazjava.booklink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.lang.reflect.Field;
import java.util.Optional;

@Configuration
public class TestLogger {

    @Bean
    @Scope("prototype")
    Logger mockLogger(InjectionPoint ip) {

        // FYI: https://medium.com/simars/inject-loggers-in-spring-java-or-kotlin-87162d02d068

        return LoggerFactory.getLogger(Optional.ofNullable(ip.getField())
                .map(Field::getDeclaringClass)
                .orElseThrow (IllegalArgumentException::new));
    }
}
