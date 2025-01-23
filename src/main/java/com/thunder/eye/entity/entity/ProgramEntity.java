package com.thunder.eye.entity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ly
 */
@Data
@TableName("program_message")
public class ProgramEntity {
    @TableField(value = "name")
    private String name;
    @TableId(value = "ip")
    private String ip;
    @TableField(value = "port")
    private int port;
    @TableField(value = "jar_name")
    private String jarName;
    @TableField(value = "jar_path")
    private String jarPath;
}
