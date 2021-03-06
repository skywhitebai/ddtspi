package com.sky.ddtspi.service.impl;

import com.sky.ddtspi.dao.custom.CustomAmazonSyncInfoMapper;
import com.sky.ddtspi.entity.AmazonSyncInfo;
import com.sky.ddtspi.entity.AmazonSyncInfoExample;
import com.sky.ddtspi.enums.AmazonSyncInfoTypeEnum;
import com.sky.ddtspi.service.IAmazonSyncInfoService;
import com.sky.ddtspi.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author baixueping
 * @description 亚马逊同步信息
 * @date 2021/5/11 16:02
 */
@Service
public class AmazonSyncInfoService implements IAmazonSyncInfoService {
    @Autowired
    CustomAmazonSyncInfoMapper customAmazonSyncInfoMapper;
    private Date lastUpdateAfter= DateUtil.strToDate("1990-01-01 00:00:00");
    @Override
    public AmazonSyncInfo getAmazonSyncInfo(AmazonSyncInfoTypeEnum typeEnum, String merchantId) {
        AmazonSyncInfoExample example=new AmazonSyncInfoExample();
        example.createCriteria().andTypeEqualTo(typeEnum.getType()).andMerchantIdEqualTo(merchantId);
        List<AmazonSyncInfo> amazonSyncInfoList=customAmazonSyncInfoMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(amazonSyncInfoList)){
            //如果不存在，则初始化一个
            AmazonSyncInfo amazonSyncInfo=new AmazonSyncInfo();
            amazonSyncInfo.setLastUpdateAfter(lastUpdateAfter);
            amazonSyncInfo.setType(typeEnum.getType());
            amazonSyncInfo.setMerchantId(merchantId);
            amazonSyncInfo.setCreateTime(new Date());
            customAmazonSyncInfoMapper.insertSelective(amazonSyncInfo);
            return amazonSyncInfo;
        }
        return amazonSyncInfoList.get(0);
    }
}
