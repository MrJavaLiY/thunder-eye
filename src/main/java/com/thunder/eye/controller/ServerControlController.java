package com.thunder.eye.controller;

import com.thunder.eye.condition.RequestCondition;
import com.thunder.eye.mode.StrategyOperate;
import com.thunder.eye.service.ServerConfigService;
import com.thunder.eye.utils.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Locale;

@RestController
@Slf4j
@RequestMapping("control")
@Api(tags = "服务器信息控制")
public class ServerControlController {
    @Resource
    StrategyOperate strategyOperate;
    @Resource
    ServerConfigService serverConfigService;
//    @GetMapping("data")
//    public ResponseEntity<List<WinCmdEntity>> winData(@RequestBody RequestCondition condition) throws Exception {

    /// /        System.out.println(fileOperService.outEntity().toString());
//        return (ResponseEntity<List<WinCmdEntity>>) strategyOperate.executeMethodSpring(condition.getServerType(), condition);
//    }
    @ApiOperation(value = "新增服务器信息")
    @PostMapping("inConfig")
    public ResponseEntity<?> inConfig(@RequestBody RequestCondition condition) {
        if ("linux".equalsIgnoreCase(condition.getServerType()) || "windows".equalsIgnoreCase(condition.getServerType())) {
            if (!condition.getServerType().startsWith("L") && !condition.getServerType().startsWith("W")) {
                condition.setServerType(condition.getServerType().substring(0, 1).toUpperCase(Locale.ROOT) + condition.getServerType().substring(1));
            }
            return serverConfigService.add(condition);
        }
        return new ResponseEntity<>().fail("服务器类型错误");
    }

    @ApiOperation(value = "删除服务器信息")
    @DeleteMapping("delConfig")
    public ResponseEntity<?> delConfig(@RequestBody RequestCondition condition) {
        return serverConfigService.del(condition);
    }

    @ApiOperation(value = "修改服务器信息")
    @PostMapping("updateConfig")
    public ResponseEntity<?> updateConfig(@RequestBody RequestCondition condition) {
        return serverConfigService.update(condition);
    }

}
