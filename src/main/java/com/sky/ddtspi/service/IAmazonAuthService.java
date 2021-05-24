package com.sky.ddtspi.service;

import com.github.pagehelper.PageInfo;
import com.sky.ddtspi.dto.amazonAuth.request.SaveAmazonAuthRequest;
import com.sky.ddtspi.dto.amazonAuth.response.ListAmazonAuthResponse;
import com.sky.ddtspi.dto.amazonAuth.request.ListAmazonAuthRequest;
import com.sky.ddtspi.dto.response.BaseResponse;
import com.sky.ddtspi.entity.AmazonAuth;

import java.util.List;

/**
 * @author baixueping
 * @description 亚马逊授权管理
 * @date 2021/4/28 16:16
 */
public interface IAmazonAuthService {
    /**
     * @param
     * @return
     * @description 查询授权信息
     * @author baixueping
     * @date 2021/4/28 17:04
     */
    BaseResponse<PageInfo<ListAmazonAuthResponse>> listAmazonAuth(ListAmazonAuthRequest params);

    /**
     * 保存token
     * @param params
     * @return
     */
    BaseResponse saveAmazonAuth(SaveAmazonAuthRequest params);
    /**
     * 查询所有亚马逊店铺授权信息
     * @return
     */
    List<AmazonAuth> listAmazonAuth();
}
