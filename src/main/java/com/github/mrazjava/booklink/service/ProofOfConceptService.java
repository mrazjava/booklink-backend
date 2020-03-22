package com.github.mrazjava.booklink.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * @since 0.1.0
 */
@Component
public class ProofOfConceptService {

    @Inject
    private Logger log;

    public int randomCount() {
        int count = new Random().nextInt();
        String uuid = UUID.randomUUID().toString();
        String now = OffsetDateTime.now().toString();
        log.debug("computed count: {} | {} - {}", count, uuid, now);
        return count;
    }
}
