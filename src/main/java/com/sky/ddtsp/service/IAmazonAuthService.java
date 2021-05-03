package com.sky.ddtsp.service;

import com.github.pagehelper.PageInfo;
import com.sky.ddtsp.dto.amazonAuth.request.SaveAmazonAuthRequest;
import com.sky.ddtsp.dto.amazonAuth.response.ListAmazonAuthResponse;
import com.sky.ddtsp.dto.amazonAuth.request.ListAmazonAuthRequest;
import com.sky.ddtsp.dto.response.BaseResponse;

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
}
