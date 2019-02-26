package com.snow.xiaoyi;

import com.snow.xiaoyi.common.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XiaoyiTests {

    @Autowired
    private SXS sxs;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test(){

        userMapper.user(1L);


    }


    @Test
    public void contextLoads() throws ExecutionException, InterruptedException {

        sxs.sayHello("ssss");
        ListenableFuture<String> xsx = sxs.sayHello2("萧毅2");
        ListenableFuture<String> ssx = sxs.sayHello2("萧毅");
        System.out.println(ssx.get());
        System.out.println(xsx.get());

    }

}
