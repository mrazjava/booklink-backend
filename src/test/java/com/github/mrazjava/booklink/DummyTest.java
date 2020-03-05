package com.github.mrazjava.booklink;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Placeholder during project setup, just to see a test executing. Ensures that
 * test suite pipeline is properly setup.
 *
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@Import(TestLogger.class)
public class DummyTest {

    @Inject
    private Logger log;

    @Test
    public void shouldRun() {
        log.info("dummy unit test: i'm the first!");
    }
}
