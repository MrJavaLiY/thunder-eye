package com.thunder.eye.utils;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ly
 * &#064;Date  2024年12月28日22:36:50
 */
@Component
public class CaffeineUtil implements CacheUtil {
    private final Cache<String, Object> cache;

    public CaffeineUtil() {
        // 初始化缓存，设置最大条目数和过期时间
        this.cache = Caffeine.newBuilder()
                .maximumSize(1000) // 最大条目数
                .expireAfterWrite(10, TimeUnit.MINUTES) // 写入后10分钟过期
                .build();
    }

    @Override
    public <T> T get(String key) {
        return (T) cache.getIfPresent(key);
    }

    @Override
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public void put(String key, Object value, long timeout) {
        // 使用 withExpireAfterWrite 创建带有自定义过期时间的新缓存条目
        Cache<String, Object> tempCache = Caffeine.newBuilder()
                .expireAfterWrite(timeout, TimeUnit.SECONDS)
                .build();
        tempCache.put(key, value);
        cache.put(key, value); // 将值放入主缓存
    }

    @Override
    public void delete(String key) {
        cache.invalidate(key);
    }

    @Override
    public boolean exists(String key) {
        return cache.getIfPresent(key) != null;
    }
    @Override
    // 获取所有缓存条目
    public Map<String, Object> getAll() {
        return cache.asMap();
    }
}
