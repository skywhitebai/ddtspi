package com.sky.ddtsp.controller;

import com.sky.ddtsp.common.annotation.Action;
import com.sky.ddtsp.common.annotation.Login;
import com.sky.ddtsp.common.constant.BaseConstant;
import com.sky.ddtsp.dto.account.request.LoginRequest;
import com.sky.ddtsp.dto.response.BaseResponse;
import com.sky.ddtsp.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @ResponseBody
    public BaseResponse login(@Validated LoginRequest params) {
        BaseResponse baseResponse=accountService.login(params);
        if(!BaseResponse.SUCCESS_CODE.equals(baseResponse.getCode())){
            return baseResponse;
        }
        // 把用户数据保存在session域对象中
        //session.setAttribute(BaseConstant.LOGIN_NAME, baseResponse.getData());
        return baseResponse;
    }
}
