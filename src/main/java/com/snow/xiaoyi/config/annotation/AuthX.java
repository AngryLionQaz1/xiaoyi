package com.snow.xiaoyi.config.annotation;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthX {

    int value() default 0;
    String name() default "测试";
    boolean flag() default false;


}
