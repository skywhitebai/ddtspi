package com.sky.ddtspi.dto.account.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author sky
 * @Description 注册入参
 * @date 2021/8/4 23:35
 */
@Data
public class RegisterRequest {
    @NotEmpty(message = "用户名不能为空")
    @Length(max = 25, message = "用户名长度不能大于25")
    String userName;
    @NotEmpty(message = "密码不能为空")
    @Length(max = 25, message = "密码长度不能大于25")
    String password;
    @NotEmpty(message = "确认密码不能为空")
    @Length(max = 25, message = "确认密码长度不能大于25")
    String confirmPassword;
    @NotEmpty(message = "手机不能为空")
    @Length(max = 25, message = "手机长度不能大于20")
    String mobile;
    @NotEmpty(message = "姓名不能为空")
    @Length(max = 25, message = "姓名长度不能大于25")
    String realName;
}
