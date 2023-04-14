package io.github.tranngockhoa.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Component
public class CacheFailoverErrorHandler implements CacheErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheFailoverErrorHandler.class);

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        LOGGER.error("Unable to get from cache => {}", cache.getName(), exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        LOGGER.error("Unable to put into cache => {}", cache.getName(), exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        LOGGER.error("Unable to evict from cache => {}", cache.getName(), exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        LOGGER.error("Unable to clean cache => {}", cache.getName(), exception);
    }
}
