package com.thunder.eye.controller;

import com.thunder.eye.entity.vo.ServerMesVo;
import com.thunder.eye.service.StateService;
import com.thunder.eye.utils.ResponseEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RequestMapping("surveillance")
@Api(tags = "监控信息获取")
public class SurveillanceController {
    @Resource
    StateService stateService;

    @GetMapping("getServerState")
    public ResponseEntity<List<ServerMesVo>> getServerState() {
        return new ResponseEntity<List<ServerMesVo>>().success(stateService.getServerState(), "获取成功");
    }
}
