package com.sky.ddtsp.controller;

import com.github.pagehelper.PageInfo;
import com.sky.ddtsp.common.annotation.Action;
import com.sky.ddtsp.common.annotation.Login;
import com.sky.ddtsp.dto.amazonAuth.AmazonConfig;
import com.sky.ddtsp.dto.amazonAuth.request.SaveAmazonAuthRequest;
import com.sky.ddtsp.dto.amazonAuth.response.ListAmazonAuthResponse;
import com.sky.ddtsp.dto.amazonAuth.request.ListAmazonAuthRequest;
import com.sky.ddtsp.dto.response.BaseResponse;
import com.sky.ddtsp.service.IAmazonAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author baixueping
 * @description 亚马逊授权管理
 * @date 2021/4/28 16:14
 */
@RestController
@RequestMapping("/amazonAuth")
public class AmazonAuthController extends BaseController {

    @Autowired
    IAmazonAuthService amazonAuthService;

    @RequestMapping("/listAmazonAuth")
    public BaseResponse<PageInfo<ListAmazonAuthResponse>> listAmazonAuth(@Validated ListAmazonAuthRequest params){
        return amazonAuthService.listAmazonAuth(params);
    }

    @RequestMapping("/saveAmazonAuth")
    public BaseResponse saveAmazonAuth(@Validated SaveAmazonAuthRequest params){
        return amazonAuthService.saveAmazonAuth(params);
    }

    @RequestMapping("/getAmazonAuthConfig")
    @Login(action = Action.Skip)
    public BaseResponse getAmazonAuthConfig(){
        return BaseResponse.successData(AmazonConfig.INSTANCE);
    }
}
