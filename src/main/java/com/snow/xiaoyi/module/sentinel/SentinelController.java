/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: SentinelController
 * Author:   萧毅
 * Date:     2019/7/1 11:18
 * Description:
 */
package com.snow.xiaoyi.module.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sentinel")
public class SentinelController {



    @GetMapping(value = "hello")
    @SentinelResource(value = "hello", blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
    public String hello() {
        return "Hello Sentinel";
    }





}