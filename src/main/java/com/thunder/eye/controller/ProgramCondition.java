package com.thunder.eye.controller;

import com.thunder.eye.entity.entity.JarDetailEntity;
import lombok.Data;

@Data
public class ProgramCondition {
    private String ip;
    private int port;
    private String name;
    private JarDetailEntity jarDetailEntity;
}
