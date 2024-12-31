package com.thunder.eye.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PollJob {
    @Resource
    MyJob myJob;

    @Scheduled(fixedDelay = 15000, initialDelay = 5000)
    public void task1() {
        myJob.heartbeat();
    }

    @Scheduled(fixedDelay = 30000, initialDelay = 10000)
    public void task2() {
        myJob.noticeDieServer();
    }

    @Scheduled(fixedDelay = 1000 * 60 * 10, initialDelay = 1000 * 60 * 10)
    public void task3() {
        myJob.closeShell();
    }
}
