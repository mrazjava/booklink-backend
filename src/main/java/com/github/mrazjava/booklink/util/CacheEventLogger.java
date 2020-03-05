package com.github.mrazjava.booklink.util;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adam Zimowski (<a href="mailto:adam.zimowski@freenetdigital.com">azimowski</a>)
 * @since 0.1.0
 */
public class CacheEventLogger implements CacheEventListener<Object, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(CacheEventLogger.class);

    @Override
    public void onEvent(CacheEvent cacheEvent) {

        LOG.info("CACHE! event={}, key={}\n* old-value={}\n* new-value={}",
                cacheEvent.getType(),
                cacheEvent.getKey(),
                cacheEvent.getOldValue(),
                cacheEvent.getNewValue()
        );
    }
}
