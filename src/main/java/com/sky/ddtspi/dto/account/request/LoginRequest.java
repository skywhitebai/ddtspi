package com.sky.ddtspi.dto.account.request;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


/**
 * @author baixueping
 * @description 登录入参
 * @date 2021/3/25 12:38
 */
@Data
public class LoginRequest {
    @NotEmpty(message = "用户名不能为空")
    String userName;
    @NotEmpty(message = "用户密码不能为空")
    String password;
}
