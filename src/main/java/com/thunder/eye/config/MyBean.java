package com.thunder.eye.config;

import com.thunder.eye.mode.mode.StrategyOperate;
import com.thunder.eye.service.LinuxService;
import com.thunder.eye.service.WinService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBean {

    @Bean
    public StrategyOperate strategyOperate() {
        return new StrategyOperate(WinService.class, LinuxService.class);
    }
}
