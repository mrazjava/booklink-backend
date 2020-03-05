package com.github.mrazjava.booklink.config;

import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * Determines which bean to use by default (interface binding), in cases where more
 * than one implementation exists.
 *
 * @since 0.1.0
 */
@Configuration
public class PrimaryBeanResolver {

    @Inject
    private Logger log;
}
