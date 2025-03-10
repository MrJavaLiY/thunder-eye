package com.thunder.eye.condition;

import lombok.Data;

@Data
public class RequestCondition {
    private String ip;
    private String user;
    private String password;
    /**
     * 服务器类型，用枚举定义，Windows和Linux两个字符
     */
    private String serverType;
    private Boolean enable;
    private String name;
    private String index;
}
