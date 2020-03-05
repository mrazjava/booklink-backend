package com.github.mrazjava.booklink.integration;

import com.github.mrazjava.booklink.TestLogger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Placeholder just to see a test executing. Ensures that IT test pipeline is properly setup.
 *
 * @since 0.1.0
 */
@RunWith(SpringRunner.class)
@Import(TestLogger.class)
public class DummyITTest {

    @Inject
    private Logger log;

    @Test
    public void shouldRun() {
        log.info("dummy integration test");
    }
}
