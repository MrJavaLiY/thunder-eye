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

    /**
     * 获取服务器状态信息
     *
     * 通过GET请求访问/getServerState端点，返回服务器当前的状态信息列表
     * 此方法利用了Spring框架的ResponseEntity类来构建响应对象，包含服务器状态信息列表和一个成功获取的消息
     *
     * @return ResponseEntity对象，包含服务器状态信息列表和成功获取的消息
     */
    @GetMapping("getServerState")
    public ResponseEntity<List<ServerMesVo>> getServerState() {
        return new ResponseEntity<List<ServerMesVo>>().success(stateService.getServerState(), "获取成功");
    }
}
