package com.sky.ddtspi.service;

import com.sky.ddtspi.entity.AmazonSyncInfo;
import com.sky.ddtspi.enums.AmazonSyncInfoTypeEnum; /**
 * @author baixueping
 * @description 亚马逊同步信息
 * @date 2021/5/11 16:01
 */
public interface IAmazonSyncInfoService {
    AmazonSyncInfo getAmazonSyncInfo(AmazonSyncInfoTypeEnum order, String merchantId);
}
