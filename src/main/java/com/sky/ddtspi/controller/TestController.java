package com.sky.ddtspi.controller;

import com.sky.ddtspi.common.annotation.Action;
import com.sky.ddtspi.common.annotation.Login;
import com.sky.ddtspi.dto.response.BaseResponse;
import com.sky.ddtspi.dto.test.request.GetLocalDateTimeRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author baixueping
 * @description TestController
 * @date 2021/7/2 9:22
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/getLocalDateTime")
    @Login(action = Action.Skip)
    @ResponseBody
    public BaseResponse getLocalDateTime(@RequestBody GetLocalDateTimeRequest prams) {
        return BaseResponse.successData(prams);
    }
}
