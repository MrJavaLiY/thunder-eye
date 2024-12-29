package com.thunder.eye.job;

import com.thunder.eye.service.HeartbeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MyJobImpl implements MyJob {
    @Resource
    HeartbeatService heartbeatService;

    @Override
    public void job1() {
        log.debug("======开始执行定时任务======");
        heartbeatService.dispatch();
        log.debug("======定时任务执行完毕======");
    }
}
