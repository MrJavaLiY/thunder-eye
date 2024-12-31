package com.thunder.eye.job;

import com.thunder.eye.service.DingNoticeService;
import com.thunder.eye.service.HeartbeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MyJobImpl implements MyJob {
    @Resource
    HeartbeatService heartbeatService;
    @Resource
    DingNoticeService dingNoticeService;

    @Override
    public void heartbeat() {
        log.debug("======开始服务器心跳检测======");
        heartbeatService.dispatch();
        log.debug("======定时服务器心跳检测完毕======");
    }

    @Override
    public void noticeDieServer() {
        log.debug("======开始死亡服务的钉钉通知======");
        dingNoticeService.noticeDieServer();
        log.debug("======死亡服务的钉钉通知完毕======");
    }
}
