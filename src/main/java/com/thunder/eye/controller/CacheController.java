package com.thunder.eye.controller;

import com.thunder.eye.utils.CacheUtil;
import com.thunder.eye.utils.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ly
 * @date 2024年12月29日22:07:07
 */
@RestController
@Slf4j
@RequestMapping("cache")
@Api(tags = "缓存管理")
public class CacheController {
    @Resource
    CacheUtil cacheUtil;

    @GetMapping("getAllCache")
    @ApiOperation(value = "获取所有缓存")
    public ResponseEntity<?> getAllCache() {
        return new ResponseEntity<>().success(cacheUtil.getAll(), "查询成功");
    }

}
