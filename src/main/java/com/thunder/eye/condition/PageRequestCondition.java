package com.thunder.eye.condition;

import lombok.Data;

@Data
public class PageRequestCondition extends RequestCondition {
    private int page =1;
    private int size = 10;
}
