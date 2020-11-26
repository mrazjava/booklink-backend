package com.github.mrazjava.booklink.util;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author AZ
 */
public class CacheEventLogger implements CacheEventListener<Object, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(CacheEventLogger.class);

    @SuppressWarnings("rawtypes")
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
