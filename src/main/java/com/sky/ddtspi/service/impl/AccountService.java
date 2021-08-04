package com.sky.ddtspi.service.impl;

import com.sky.ddtspi.common.constant.UserConstant;
import com.sky.ddtspi.common.login.CurrentUserInfo;
import com.sky.ddtspi.common.login.LoginHelper;
import com.sky.ddtspi.common.response.AccountEnum;
import com.sky.ddtspi.dao.custom.CustomUserMapper;
import com.sky.ddtspi.dto.account.request.ChangePasswordRequest;
import com.sky.ddtspi.dto.account.request.LoginRequest;
import com.sky.ddtspi.dto.account.request.RegisterRequest;
import com.sky.ddtspi.dto.response.BaseResponse;
import com.sky.ddtspi.entity.SysUser;
import com.sky.ddtspi.entity.SysUserExample;
import com.sky.ddtspi.service.IAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author baixueping
 * @description 账号服务
 * @date 2021/3/25 12:18
 */
@Service
public class AccountService implements IAccountService {
    @Autowired
    CustomUserMapper customUserMapper;
    @Autowired
    LoginHelper loginHelper;
    /**
     * @param params@return
     * @description 登录
     * @author baixueping
     * @date 2021/3/25 12:39
     */
    @Override
    public BaseResponse login(LoginRequest params) {
        SysUserExample userExample=new SysUserExample();
        userExample.createCriteria().andUserNameEqualTo(params.getUserName());
        List<SysUser> userList= customUserMapper.selectByExample(userExample);
        if(userList.size()==0){;
            return BaseResponse.baseEnum(AccountEnum.LOGIN_USER_NAME_NOT_EXIST);
        }
        SysUser user=userList.get(0);
        if(!DigestUtils.md5DigestAsHex(params.getPassword().getBytes()).equals(user.getPassword())){
            return BaseResponse.baseEnum(AccountEnum.LOGIN_PASSWORD_ERRO);
        }
        if(!UserConstant.STATUS_ENABLE.equals(user.getStatus())){
            return BaseResponse.baseEnum(AccountEnum.USER_DISABLE);
        }
        SysUser userUpdate=new SysUser();
        userUpdate.setLastLoginTime(new Date());
        if(user.getLoginTimes()==null||user.getLoginTimes()<=0){
            userUpdate.setLoginTimes(1);
        }else{
            userUpdate.setLoginTimes(user.getLoginTimes()+1);
        }
        userUpdate.setId(user.getId());
        customUserMapper.updateByPrimaryKeySelective(userUpdate);
        CurrentUserInfo currentUserInfo=new CurrentUserInfo();
        BeanUtils.copyProperties(user,currentUserInfo);
        currentUserInfo.setUserId(user.getId());
        currentUserInfo.setLoginToken(user.getId()+"-"+ UUID.randomUUID().toString());
        loginHelper.saveLoginToken(currentUserInfo);
        return BaseResponse.successData(currentUserInfo);
    }

    @Override
    public BaseResponse changePassword(ChangePasswordRequest params, Long currentUserId) {
        SysUser sysUser=customUserMapper.selectByPrimaryKey(currentUserId);
        if(sysUser==null){
            return BaseResponse.failMessage("用户id不存在");
        }
        if(!params.getNewPassword().equals(params.getConfirmNewPassword())){
            return BaseResponse.failMessage("新密码喝确认新密码必须一致");
        }
        if(!DigestUtils.md5DigestAsHex(params.getOldPassword().getBytes()).equals(sysUser.getPassword())){
            return BaseResponse.failMessage("原密码错误");
        }
        sysUser.setPassword(DigestUtils.md5DigestAsHex(params.getNewPassword().getBytes()));
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateBy(currentUserId);
        customUserMapper.updateByPrimaryKey(sysUser);
        return BaseResponse.success();
    }

    @Override
    public BaseResponse register(RegisterRequest params) {
        return null;
    }
}
