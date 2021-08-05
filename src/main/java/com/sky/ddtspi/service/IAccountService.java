package com.sky.ddtspi.service;

import com.sky.ddtspi.dto.account.request.ChangePasswordRequest;
import com.sky.ddtspi.dto.account.request.LoginRequest;
import com.sky.ddtspi.dto.account.request.RegisterRequest;
import com.sky.ddtspi.dto.response.BaseResponse;

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

    /**
     * @param params
     * @param currentUserId
     * @return
     * @author sky
     * @Description: 修改密码
     * @date
     */
    BaseResponse changePassword(ChangePasswordRequest params, Long currentUserId);

    /**
     * @param
     * @return
     * @author sky
     * @Description: 注册
     * @date
     */
    BaseResponse register(RegisterRequest params);
}
