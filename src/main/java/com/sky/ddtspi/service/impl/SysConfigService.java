package com.sky.ddtspi.service.impl;

import com.sky.ddtspi.dao.custom.CustomSysConfigMapper;
import com.sky.ddtspi.entity.SysConfig;
import com.sky.ddtspi.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysConfigService implements ISysConfigService {
    @Autowired
    CustomSysConfigMapper customSysConfigMapper;

    @Override
    public List<SysConfig> listAllSysConfig() {
        return customSysConfigMapper.selectByExample(null);
    }
}
