package com.thunder.eye.entity.sql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ly
 */
@Data
@TableName("server_config")
public class ServerConfig {
    @TableId(value = "id")
    private Long id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "ip")
    private String ip;
    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password;
}
