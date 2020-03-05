package com.github.mrazjava.booklink.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Random;

/**
 * @since 0.1.0
 */
@Component
public class BookService {

    @Inject
    private Logger log;

    public int totalCount() {
        int count = new Random().nextInt();
        log.debug("computed count: {}", count);
        return count;
    }
}
