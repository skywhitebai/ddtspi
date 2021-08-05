package com.sky.ddtspi;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author baixueping
 * @description 测试基类
 * @date 2021/7/12 9:46
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@PropertySource("classpath:test.properties")
public class BaseTest {
}
