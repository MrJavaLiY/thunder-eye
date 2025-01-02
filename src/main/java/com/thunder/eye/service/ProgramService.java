package com.thunder.eye.service;

import com.thunder.eye.controller.ProgramCondition;
import com.thunder.eye.entity.entity.ProgramEntity;

import java.util.List;

public interface ProgramService {


    int insertProgramMessage(ProgramCondition condition);

    int updateProgramMessage(ProgramCondition condition);

    int deleteProgramMessage(ProgramCondition condition);

    List<ProgramEntity> selectProgramMessage(ProgramCondition condition);

    Long getProgramCount(ProgramCondition condition);
}
