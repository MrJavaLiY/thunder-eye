package com.thunder.eye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thunder.eye.controller.ProgramCondition;
import com.thunder.eye.dao.ProgramMapper;
import com.thunder.eye.entity.entity.ProgramEntity;
import com.thunder.eye.service.ProgramService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class ProgramServiceImpl implements ProgramService {
    @Resource
    ProgramMapper programMapper;

    @Override
    public int insertProgramMessage(ProgramCondition condition) {
        Long getProgramCount = getProgramCount(condition);
        if (getProgramCount > 0) {
            return 0;
        }
        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setIp(condition.getJarDetailEntity().getIp());
        programEntity.setPort(condition.getJarDetailEntity().getPort());
        programEntity.setName("默认名称");
        programEntity.setJarName(condition.getJarDetailEntity().getJarName());
        programEntity.setJarPath(condition.getJarDetailEntity().getJarPath());
        return programMapper.insert(programEntity);
    }

    @Override
    public int updateProgramMessage(ProgramCondition condition) {
        if (!StringUtils.hasText(condition.getIp()) || condition.getPort() == 0) {
            throw new RuntimeException("请传入ip和端口");
        }
        if (StringUtils.hasText(condition.getName())) {
            ProgramEntity programEntity = new ProgramEntity();
            programEntity.setName(condition.getName());
            programEntity.setIp(condition.getIp());
            programEntity.setPort(condition.getPort());
            return programMapper.updateById(programEntity);
        }
        throw new RuntimeException("请传入名称");
    }

    @Override
    public int deleteProgramMessage(ProgramCondition condition) {
        if (!StringUtils.hasText(condition.getIp()) || condition.getPort() == 0) {
            throw new RuntimeException("请传入ip和端口");
        }
        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setIp(condition.getIp());
        programEntity.setPort(condition.getPort());
        return programMapper.deleteById(programEntity);
    }

    @Override
    public List<ProgramEntity> selectProgramMessage(ProgramCondition condition) {
        return programMapper.selectList(getQuery(condition));
    }

    @Override
    public Long getProgramCount(ProgramCondition condition) {


        return programMapper.selectCount(getQuery(condition));
    }

    private QueryWrapper<ProgramEntity> getQuery(ProgramCondition condition) {
        QueryWrapper<ProgramEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(condition.getIp())) {
            wrapper.eq("ip", condition.getIp());
        }
        if (condition.getPort() != 0) {
            wrapper.eq("port", condition.getPort());
        }
        if (StringUtils.hasText(condition.getName())) {
            wrapper.eq("name", condition.getName());
        }
        return wrapper;
    }
}
