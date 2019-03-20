/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: ss
 * Author:   萧毅
 * Date:     2019/3/20 14:31
 * Description:
 */
package com.snow.xiaoyi;


import org.junit.Test;

public class ss {


    @Test
    public void test(){


        System.out.println(UTF_8ToGBK("好"));
        System.out.println(UTF8ToGBK("好"));
        System.out.println(GBK("好"));


    }

    public static String UTF_8ToGBK(String str) {
        try {
            return new String(str.getBytes("UTF-8"), "GBK");
        } catch (Exception ex) {
            return null;
        }
    }
    public static String UTF8ToGBK(String str) {
        try {
            return new String(str.getBytes("UTF-16BE"), "GBK");
        } catch (Exception ex) {
            return null;
        }
    }
    public static String GBK(String str) {
        try {
            return new String(str.getBytes("GBK"),"GBK");
        } catch (Exception ex) {
            return null;
        }
    }

}