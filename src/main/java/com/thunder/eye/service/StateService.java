package com.thunder.eye.service;

import com.thunder.eye.entity.entity.JarDetailEntity;
import com.thunder.eye.entity.vo.ServerMesVo;

import java.util.List;

public interface StateService {

    List<ServerMesVo> getServerState();

    List<JarDetailEntity> getDieServer();
}
