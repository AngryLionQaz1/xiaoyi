/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: ExceptionUtil
 * Author:   萧毅
 * Date:     2019/7/1 11:39
 * Description:
 */
package com.snow.xiaoyi.module.sentinel;


import com.alibaba.csp.sentinel.slots.block.BlockException;

public class ExceptionUtil {


    public static String handleException(BlockException ex) {

        System.out.println("Oops: " + ex.getClass().getCanonicalName());

        return "Oops: " + ex.getClass().getCanonicalName();
    }

}