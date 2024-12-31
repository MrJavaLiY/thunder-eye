package com.thunder.eye.entity.vo;

import com.thunder.eye.entity.entity.JarDetailEntity;
import lombok.Data;

import java.util.List;

@Data
public class ServerMesVo {
    private String ip;
    private List<JarDetailEntity> jarDetails;
}
