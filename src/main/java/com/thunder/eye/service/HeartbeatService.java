package com.thunder.eye.service;

import com.thunder.eye.utils.CacheUtil;

public interface HeartbeatService {
    CacheUtil getCacheUtil();
    /**
     * 控制中枢
     */
    void dispatch();
}
