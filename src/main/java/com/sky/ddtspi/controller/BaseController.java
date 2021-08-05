package com.sky.ddtspi.controller;

import com.sky.ddtspi.common.login.CurrentUserInfo;
import com.sky.ddtspi.common.login.LoginHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author baixueping
 * @description 控制器
 * @date 2021/3/23 17:26
 */

public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;
    @Autowired
    LoginHelper loginHelper;
    public CurrentUserInfo getCurrentUserInfo(){
       return loginHelper.getCurrentUserInfo(request);
    }
    public Long getCurrentUserId(){
        CurrentUserInfo currentUserInfo= loginHelper.getCurrentUserInfo(request);
        if(currentUserInfo==null){
            return null;
        }
        return currentUserInfo.getUserId();
    }
}
