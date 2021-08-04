package com.sky.ddtspi.controller;

import com.sky.ddtspi.dto.response.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author sky
 * @Description TODO
 * @date 2021/6/30 23:49
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/getLocalTime")
    public BaseResponse getLocalTime(){
        return BaseResponse.successData(LocalDateTime.now());
    }

}
