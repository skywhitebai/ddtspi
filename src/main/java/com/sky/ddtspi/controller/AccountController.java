package com.sky.ddtspi.controller;

import com.sky.ddtspi.common.annotation.Action;
import com.sky.ddtspi.common.annotation.Login;
import com.sky.ddtspi.common.constant.BaseConstant;
import com.sky.ddtspi.dto.account.request.ChangePasswordRequest;
import com.sky.ddtspi.dto.account.request.LoginRequest;
import com.sky.ddtspi.dto.account.request.RegisterRequest;
import com.sky.ddtspi.dto.response.BaseResponse;
import com.sky.ddtspi.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author baixueping
 * @description 账户控制器
 * @date 2021/3/25 12:36
 */
@RestController
@RequestMapping("/account")
public class AccountController extends BaseController {
    @Autowired
    IAccountService accountService;
    @RequestMapping("/login")
    @Login(action = Action.Skip )
    public BaseResponse login(@Validated LoginRequest params) {
        BaseResponse baseResponse=accountService.login(params);
        if(!BaseResponse.SUCCESS_CODE.equals(baseResponse.getCode())){
            return baseResponse;
        }
        // 把用户数据保存在session域对象中 前后端分离用不了，只能用token
        //session.setAttribute(BaseConstant.LOGIN_NAME, baseResponse.getData());
        return baseResponse;
    }
    @RequestMapping("/changePassword")
    public BaseResponse changePassword(@Validated ChangePasswordRequest params) {
        BaseResponse baseResponse=accountService.changePassword(params,getCurrentUserId());
        return baseResponse;
    }
    @RequestMapping("/register")
    @Login(action = Action.Skip )
    public BaseResponse register(@Validated RegisterRequest params) {
        BaseResponse baseResponse=accountService.register(params);
        return baseResponse;
    }
}
