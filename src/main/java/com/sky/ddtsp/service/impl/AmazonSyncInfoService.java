package com.sky.ddtsp.service.impl;

import com.sky.ddtsp.dao.custom.CustomAmazonSyncInfoMapper;
import com.sky.ddtsp.service.IAmazonSyncInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author baixueping
 * @description 亚马逊同步信息
 * @date 2021/5/11 16:02
 */
@Service
public class AmazonSyncInfoService implements IAmazonSyncInfoService {
    @Autowired
    CustomAmazonSyncInfoMapper customAmazonSyncInfoMapper;
}
