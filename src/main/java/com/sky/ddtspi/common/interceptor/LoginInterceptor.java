package com.sky.ddtspi.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.sky.ddtspi.common.annotation.Action;
import com.sky.ddtspi.common.annotation.Login;
import com.sky.ddtspi.common.login.CurrentUserInfo;
import com.sky.ddtspi.common.login.LoginHelper;
import com.sky.ddtspi.common.response.AccountEnum;
import com.sky.ddtspi.dto.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 登录拦截器，做登录校验
 *
 * @author Administrator
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    LoginHelper loginHelper;
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    /**
     * 登录权限验证
     * <p>
     * 方法拦截 Controller 处理之前进行调用。
     * </p>
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        /**
         * 处理 Controller 方法
         * <p>
         * 判断 handler 是否为 HandlerMethod 实例
         * </p>
         */
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if(handlerMethod.getBeanType().equals(BasicErrorController.class)){
                return true;
            }
            Method method = handlerMethod.getMethod();
            Boolean needLogin = false;
            Boolean needMenu = false;
            Boolean needRight = false;
            Login login = method.getAnnotation(Login.class);
            if (login == null || login.action() != Action.Skip) {
                needLogin = true;
            }
            if (needLogin == false) {
                return true;
            }
            /**
             * 正常执行 判断是否登录
             */
            CurrentUserInfo currentUserInfo = loginHelper.getCurrentUserInfo(request);
            if (currentUserInfo == null) {
                getUnLoginReponse(response);
                return false;
            }
            return true;
        }

        /**
         * 通过拦截
         */
        return true;
    }

    private void getUnLoginReponse(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(401);
        PrintWriter out = response.getWriter();
        BaseResponse baseResponse = BaseResponse.baseEnum(AccountEnum.LOGIN_DISABLE);
        out.print(JSON.toJSON(baseResponse));
        out.flush();
    }

}
