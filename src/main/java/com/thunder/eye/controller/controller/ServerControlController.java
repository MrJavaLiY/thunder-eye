package com.thunder.eye.controller.controller;

import com.thunder.eye.condition.RequestCondition;
import com.thunder.eye.mode.mode.StrategyOperate;
import com.thunder.eye.service.FileOperService;
import com.thunder.eye.utils.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("control")
@Api(tags = "服务器信息控制")
public class ServerControlController {
    @Resource
    StrategyOperate strategyOperate;
    @Resource
    FileOperService fileOperService;

//    @GetMapping("data")
//    public ResponseEntity<List<WinCmdEntity>> winData(@RequestBody RequestCondition condition) throws Exception {
////        System.out.println(fileOperService.outEntity().toString());
//        return (ResponseEntity<List<WinCmdEntity>>) strategyOperate.executeMethodSpring(condition.getServerType(), condition);
//    }
    @ApiOperation(value = "新增服务器信息")
    @PostMapping("inConfig")
    public ResponseEntity<?> inConfig(@RequestBody RequestCondition condition) {
        return  fileOperService.addData2File(condition);
    }
    @ApiOperation(value = "删除服务器信息")
    @DeleteMapping("delConfig")
    public ResponseEntity<?> delConfig(@RequestBody RequestCondition condition) {
        return fileOperService.deleteData2File(condition);
    }
    @ApiOperation(value = "修改服务器信息")
    @PostMapping("updateConfig")
    public ResponseEntity<?> updateConfig(@RequestBody RequestCondition condition) {
        return fileOperService.updateData2File(condition);
    }

}
