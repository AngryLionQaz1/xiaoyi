package com.snow.xiaoyi.common.util;

public class StringUtils {



    public static boolean isNull(Object obj){
        if (obj==null||"".equals(obj))return true;
        return false;
    }

    public static boolean isNotNull(Object obj){
        return !isNull(obj);
    }













}
