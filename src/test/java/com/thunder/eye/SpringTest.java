package com.thunder.eye;

import com.thunder.eye.service.HeartbeatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class SpringTest {

    @Resource
    HeartbeatService heartbeatService;

    @Test
    public void test() {
        heartbeatService.dispatch();
    }
}
