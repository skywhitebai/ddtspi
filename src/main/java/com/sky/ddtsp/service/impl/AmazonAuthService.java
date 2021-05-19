package com.sky.ddtsp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.ddtsp.dao.custom.CustomAmazonAuthMapper;
import com.sky.ddtsp.dto.amazonAuth.AmazonConfig;
import com.sky.ddtsp.dto.amazonAuth.request.SaveAmazonAuthRequest;
import com.sky.ddtsp.dto.amazonAuth.response.ListAmazonAuthResponse;
import com.sky.ddtsp.dto.amazonAuth.request.ListAmazonAuthRequest;
import com.sky.ddtsp.dto.response.BaseResponse;
import com.sky.ddtsp.entity.AmazonAuth;
import com.sky.ddtsp.entity.AmazonAuthExample;
import com.sky.ddtsp.enums.YesOrNoEnum;
import com.sky.ddtsp.service.IAmazonAuthService;
import com.sky.ddtsp.util.HttpTool;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author baixueping
 * @description 亚马逊授权服务
 * @date 2021/4/28 16:17
 */
@Service
public class AmazonAuthService implements IAmazonAuthService {

    @Autowired
    CustomAmazonAuthMapper customAmazonAuthMapper;

    /**
     * @param params@return
     * @description 查询授权信息
     * @author baixueping
     * @date 2021/4/28 17:04
     */
    @Override
    public BaseResponse<PageInfo<ListAmazonAuthResponse>> listAmazonAuth(ListAmazonAuthRequest params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        AmazonAuthExample amazonAuthExample = new AmazonAuthExample();
        AmazonAuthExample.Criteria criteria = amazonAuthExample.createCriteria();
        if (!StringUtils.isEmpty(params.getMerchantId())) {
            criteria.andMerchantIdLike(params.getMerchantId());
        }
        if (!StringUtils.isEmpty(params.getShopName())) {
            criteria.andShopNameLike(params.getShopName());
        }
        if (params.getStatus() != null) {
            criteria.andStatusEqualTo(params.getStatus());
        }
        List<AmazonAuth> amazonAuthList = customAmazonAuthMapper.selectByExample(amazonAuthExample);
        List<ListAmazonAuthResponse> listAmazonAuthResponseList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(amazonAuthList)) {
            amazonAuthList.forEach(item -> {
                ListAmazonAuthResponse listAmazonAuthResponse = new ListAmazonAuthResponse();
                BeanUtils.copyProperties(item, listAmazonAuthResponse);
                listAmazonAuthResponseList.add(listAmazonAuthResponse);
            });
        }
        PageInfo<ListAmazonAuthResponse> pageInfo = new PageInfo<>(listAmazonAuthResponseList);
        return BaseResponse.successData(pageInfo);
    }

    @Override
    public BaseResponse saveAmazonAuth(SaveAmazonAuthRequest params) {
        String url = "https://api.amazon.com/auth/o2/token";
        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "authorization_code");
        param.put("code", params.getSpapiOauthCode());
        param.put("client_id", AmazonConfig.INSTANCE.getAmazonAppClientId());
        param.put("client_secret", AmazonConfig.INSTANCE.getAmazonAppClientSecret());
        String res = HttpTool.doPost(url, param);
        if (StringUtils.isEmpty(res)) {
            return BaseResponse.failMessage("授权失败");
        }
        JSONObject jsonObject=JSONObject.parseObject(res);
        String refreshToken=jsonObject.getString("refresh_token");
        AmazonAuth amazonAuthExist=getAmazonAuthByMerchantId(params.getSellingPartnerId());
        if(amazonAuthExist!=null){
            AmazonAuth amazonAuthUpdate=new AmazonAuth();
            amazonAuthUpdate.setId(amazonAuthExist.getId());
            amazonAuthUpdate.setRefreshToken(refreshToken);
            amazonAuthUpdate.setMarketplaceId("ATVPDKIKX0DER");
            customAmazonAuthMapper.updateByPrimaryKeySelective(amazonAuthUpdate);
        }else {
            AmazonAuth amazonAuth=new AmazonAuth();
            amazonAuth.setMerchantId(params.getSellingPartnerId());
            amazonAuth.setRefreshToken(refreshToken);
            amazonAuth.setCreateTime(new Date());
            customAmazonAuthMapper.insertSelective(amazonAuth);
        }
        return BaseResponse.success();
    }

    @Override
    public List<AmazonAuth> listAmazonAuth() {
        AmazonAuthExample amazonAuthExample = new AmazonAuthExample();
        amazonAuthExample.createCriteria().andStatusEqualTo(YesOrNoEnum.YES.getValue());
        return customAmazonAuthMapper.selectByExample(amazonAuthExample);
    }

    private AmazonAuth getAmazonAuthByMerchantId(String sellingPartnerId) {
        AmazonAuthExample amazonAuthExample=new AmazonAuthExample();
        amazonAuthExample.createCriteria().andMerchantIdEqualTo(sellingPartnerId);
        List<AmazonAuth> list=customAmazonAuthMapper.selectByExample(amazonAuthExample);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }
}
