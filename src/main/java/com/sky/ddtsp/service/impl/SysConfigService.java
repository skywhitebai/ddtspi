package com.sky.ddtsp.service.impl;

import com.sky.ddtsp.dao.custom.CustomSysConfigMapper;
import com.sky.ddtsp.entity.SysConfig;
import com.sky.ddtsp.service.ISysConfigService;
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
