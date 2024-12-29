package com.thunder.eye.utils;

import java.util.Map;

/**
 * @author ly
 */
public interface CacheUtil {
    /**
     * 根据键获取缓存中的值
     *
     * @param key 缓存键
     * @param <T> 返回值的类型
     * @return 缓存中的值，如果不存在则返回 null
     */
    <T> T get(String key);

    /**
     * 将键值对存入缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    void put(String key, Object value);

    /**
     * 将键值对存入缓存，并设置过期时间
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间（单位：秒）
     */
    void put(String key, Object value, long timeout);

    /**
     * 删除指定键的缓存
     *
     * @param key 缓存键
     */
    void delete(String key);

    /**
     * 检查缓存中是否存在指定键
     *
     * @param key 缓存键
     * @return 如果存在则返回 true，否则返回 false
     */
    boolean exists(String key);
    // 获取所有缓存条目
     Map<String, Object> getAll() ;
}
