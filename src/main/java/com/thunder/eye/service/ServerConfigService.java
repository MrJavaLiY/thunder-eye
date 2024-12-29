package com.thunder.eye.service;

import com.thunder.eye.condition.RequestCondition;
import com.thunder.eye.utils.ResponseEntity;

public interface ServerConfigService {

    public ResponseEntity<?> add(RequestCondition condition);

    public ResponseEntity<?> del(RequestCondition condition);

    public ResponseEntity<?> update(RequestCondition condition);

    public ResponseEntity<?> getOne(RequestCondition condition);

    public ResponseEntity<?> getAll();
    public ResponseEntity<?> getPage(RequestCondition condition);
    public ResponseEntity<?> getAllEnable();
}
