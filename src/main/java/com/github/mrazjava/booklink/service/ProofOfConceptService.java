package com.github.mrazjava.booklink.service;

import com.github.mrazjava.booklink.util.WordGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @since 0.1.0
 */
@Component
public class ProofOfConceptService {

    @Inject
    private Logger log;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private WordGenerator wordGenerator;

    public int randomCount() {
        int count = new Random().nextInt();
        String uuid = UUID.randomUUID().toString();
        String now = OffsetDateTime.now().toString();
        log.debug("computed count: {} | {} - {}", count, uuid, now);
        return count;
    }

    public Set<String> randomWords() {
        int wordCount = ThreadLocalRandom.current().nextInt(1, 11);
        Set<String> words = new HashSet<>();
        for(int x=0; x<wordCount; x++) {
            words.add(wordGenerator.newWord(ThreadLocalRandom.current().nextInt(3, 11)));
        }
        return words;
    }

    public String randomAlpha() {
        return RandomStringUtils.randomAlphabetic(15);
    }

    public String randomAlphanumeric() {
        return RandomStringUtils.randomAlphanumeric(ThreadLocalRandom.current().nextInt(2, 21));
    }

    public String sayHello(Authentication auth) {
        return "Howdy, it's " + new Date() + " (and you are: " + auth.getName() + ")";
    }

    public String getEncodedPassword(String plainText) {
        return passwordEncoder.encode(plainText);
    }
}
