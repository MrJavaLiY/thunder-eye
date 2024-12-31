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

// 新增服务器信息的API操作方法
@ApiOperation(value = "新增服务器信息")
@PostMapping("inConfig")
public ResponseEntity<?> inConfig(@RequestBody RequestCondition condition) {
    // 检查服务器类型是否为Linux或Windows，并修正服务器类型首字母大写
    if ("linux".equalsIgnoreCase(condition.getServerType()) || "windows".equalsIgnoreCase(condition.getServerType())) {
        // 如果服务器类型不以"L"或"W"开头，修正服务器类型首字母大写
        if (!condition.getServerType().startsWith("L") && !condition.getServerType().startsWith("W")) {
            condition.setServerType(condition.getServerType().substring(0, 1).toUpperCase(Locale.ROOT) + condition.getServerType().substring(1));
        }
        // 调用服务配置的添加方法
        return serverConfigService.add(condition);
    }
    // 如果服务器类型不正确，返回错误信息
    return new ResponseEntity<>().fail("服务器类型错误");
}


    /**
     * 删除服务器信息
     *
     * 该接口用于删除特定的服务器配置信息它期望接收一个请求体，其中包含删除条件，
     * 并返回删除操作的结果
     *
     * @param condition 包含删除条件的请求体
     * @return 返回表示删除操作结果的响应实体
     */
    @ApiOperation(value = "删除服务器信息")
    @DeleteMapping("delConfig")
    public ResponseEntity<?> delConfig(@RequestBody RequestCondition condition) {
        return serverConfigService.del(condition);
    }

    /**
     * 修改服务器信息
     *
     * 该方法通过接收一个RequestCondition对象来更新服务器配置信息
     * 使用POST请求，请求体中应包含需要更新的服务器配置信息
     *
     * @param condition 包含要更新的服务器信息的请求条件对象
     * @return 返回更新操作的结果，以ResponseEntity包装
     */
    @ApiOperation(value = "修改服务器信息")
    @PostMapping("updateConfig")
    public ResponseEntity<?> updateConfig(@RequestBody RequestCondition condition) {
        return serverConfigService.update(condition);
    }

}
