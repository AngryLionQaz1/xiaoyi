/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: SXS
 * Author:   萧毅
 * Date:     2019/2/26 10:28
 * Description:
 */
package com.snow.xiaoyi;


import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class SXS {

    @Async
    public ListenableFuture<String> sayHello2(String name) {
        String res = name + ":Hello World!";
        LoggerFactory.getLogger(SXS.class).info(res);
        return new AsyncResult<>(res);
    }
    @Async
    public void sayHello(String name) {
        LoggerFactory.getLogger(SXS.class).info(name + ":Hello World!");
    }


}