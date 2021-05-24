package com.sky.ddtspi.common.login;

import com.sky.ddtspi.common.constant.BaseConstant;
import com.sky.ddtspi.service.IUserService;
import com.sky.ddtspi.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoginHelper {
    @Autowired
    IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(LoginHelper.class);
    public static final Map<String, CurrentUserInfo> loginTokenMap = new HashMap<>();
    public static final List<CurrentUserInfo> currentUserInfoList = new ArrayList<>();
    private static final Integer loginTokenExpireDay=1;

    /**
     * 通过session获取当前用户信息
     *
     * @param request
     * @return
     */
    private CurrentUserInfo getCurrentUserInfoBySession(HttpServletRequest request) {
        //通过网页登录的话从session里面拿loginToken
        HttpSession httpSession = request.getSession();
        Object object = httpSession.getAttribute(BaseConstant.LOGIN_NAME);
        if (object == null) {
            return null;
        }
        try {
            return (CurrentUserInfo) object;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取session错误,{}", ex);
            return null;
        }
    }

    /**
     * 通过map获取当前用户信息
     *
     * @param request
     * @return
     */
    private CurrentUserInfo getCurrentUserInfoByMap(HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return null;
        }
        CurrentUserInfo currentUserInfo = loginTokenMap.get(token);
        if (currentUserInfo == null) {
            logger.info("loginToken：{}，currentUserInfo not exists", token);
        }
        return currentUserInfo;
    }

    private CurrentUserInfo getCurrentUserInfoByList(HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return null;
        }
        Optional<CurrentUserInfo> currentUserInfoFirst = currentUserInfoList.stream().filter(item -> (item.getLoginToken().equals(token))).findFirst();
        if (!currentUserInfoFirst.isPresent()) {
            logger.info("loginToken：{}，currentUserInfo not exists", token);
            return null;
        }
        CurrentUserInfo currentUserInfo = currentUserInfoFirst.get();
        return currentUserInfo;
    }

    private Long getCurrentUserIdByMap(HttpServletRequest request) {
        CurrentUserInfo currentUserInfo = getCurrentUserInfoByMap(request);
        if (currentUserInfo == null) {
            return null;
        }
        return currentUserInfo.getUserId();
    }

    private Long getCurrentUserIdByList(HttpServletRequest request) {
        CurrentUserInfo currentUserInfo = getCurrentUserInfoByList(request);
        if (currentUserInfo == null) {
            return null;
        }
        currentUserInfo.setExpireTime(DateUtil.plusDay(loginTokenExpireDay, new Date()));
        return currentUserInfo.getUserId();
    }

    /**
     * 获取用户ID
     */
    public Long getCurrentUserId(HttpServletRequest request) {
        Long currentUserId = getCurrentUserIdByList(request);
        return currentUserId;
    }

    /**
     * 通过session获取当前用户id
     *
     * @param request
     * @return
     */
    private Long getCurrentUserIdBySession(HttpServletRequest request) {
        CurrentUserInfo currentUserInfo = getCurrentUserInfoBySession(request);
        if (currentUserInfo == null) {
            return null;
        } else {
            return currentUserInfo.getUserId();
        }
    }

    /**
     * 获取LoginToken
     *
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request) {
        String loginToken = request.getHeader(BaseConstant.LOGIN_NAME);
        if (StringUtils.isEmpty(loginToken)) {
            loginToken = request.getParameter(BaseConstant.LOGIN_NAME);
            if (!StringUtils.isEmpty(loginToken)) {
                request.setAttribute(BaseConstant.LOGIN_NAME, loginToken);
            }
        }
        if (StringUtils.isEmpty(loginToken)) {
            Object token = request.getAttribute(BaseConstant.LOGIN_NAME);
            if (token != null) {
                loginToken = token.toString();
            }
        }
        if (!StringUtils.isEmpty(loginToken)) {
            logger.info("loginToken：{}", loginToken.toString());
            return loginToken.toString();
        } else {
            logger.info("loginToken：null");
            return loginToken;
        }
    }

    public CurrentUserInfo getCurrentUserInfo(HttpServletRequest request) {
        return getCurrentUserInfoByList(request);
    }

    public void saveLoginToken(CurrentUserInfo currentUserInfo) {
        //设置一天后token过期
        currentUserInfo.setExpireTime(DateUtil.plusDay(loginTokenExpireDay, new Date()));
        saveLoginTokenByList(currentUserInfo);
    }

    //允许同一个账号多地登录
    private void saveLoginTokenByList(CurrentUserInfo currentUserInfo) {
        if (currentUserInfoList.size() > 10) {
            List<CurrentUserInfo> expireList = currentUserInfoList.stream().filter(item -> (item.getExpireTime().before(new Date()))).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(expireList)) {
                expireList.forEach(item -> {
                    currentUserInfoList.remove(item);
                });
            } else {
                currentUserInfoList.sort(Comparator.comparing(CurrentUserInfo::getExpireTime));
                currentUserInfoList.remove(0);
            }
        }
        currentUserInfoList.add(currentUserInfo);
    }
}
