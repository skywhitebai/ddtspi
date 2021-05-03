package com.sky.ddtsp.service;

import com.sky.ddtsp.dto.account.request.LoginRequest;
import com.sky.ddtsp.dto.response.BaseResponse;

/**
 * @author baixueping
 * @description 账号服务
 * @date 2021/3/25 12:17
 */
public interface IAccountService {
    /**
     * @param
     * @return
     * @description 登录
     * @author baixueping
     * @date 2021/3/25 12:39
     */
    BaseResponse login(LoginRequest params);
}
