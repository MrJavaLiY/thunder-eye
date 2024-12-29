package com.thunder.eye.service;


import com.thunder.eye.annotation.Strategy;
import com.thunder.eye.annotation.StrategyPoint;
import com.thunder.eye.entity.entity.JarDetailEntity;
import com.thunder.eye.entity.sql.ServerConfig;
import com.thunder.eye.utils.ResponseEntity;

import java.util.List;

/**
 * @author ly
 */
@StrategyPoint
public interface WinService {
    @Strategy(value = "Windows")
    ResponseEntity<List<JarDetailEntity>> dispatch(ServerConfig serverConfig) throws Exception;
}
