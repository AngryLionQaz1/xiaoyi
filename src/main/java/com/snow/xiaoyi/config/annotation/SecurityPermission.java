package com.snow.xiaoyi.config.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecurityPermission {

    String value()  default "";





}

