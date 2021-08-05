package com.sky.ddtspi.dto.account.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author sky
 * @Description 修改密码入参
 * @date 2021/8/4 21:21
 */
@Data
public class ChangePasswordRequest {
    @NotEmpty(message = "原密码不能为空")
    @Length(max = 25, message = "原密码长度不能大于25")
    String oldPassword;
    @NotEmpty(message = "新密码不能为空")
    @Length(max = 25, message = "新密码长度不能大于25")
    String newPassword;
    @NotEmpty(message = "确认新密码不能为空")
    String confirmNewPassword;
}
