package com.sky.ddtspi.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.ddtspi.BaseTest;
import com.sky.ddtspi.DdtspApplication;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Properties;

/**
 * @author baixueping
 * @description Test1
 * @date 2021/7/12 9:48
 */
public class Test1 extends BaseTest {
    @Value("${test.name}")
    private String testName;
    @Test
    public void testProperties() {
        Properties p = System.getProperties();
        System.out.println(JSONObject.toJSON(p));
        System.out.println("pagehelper.helperDialect:"+p.getProperty("pagehelper.helperDialect"));
        System.out.println("testName:"+testName);


    }
}
