package com.snow.xiaoyi.config.security;

import org.springframework.stereotype.Component;

@Component
public  class SecurityContextHolder {


    private static ThreadLocal<Long> securityContext=new ThreadLocal<>();

    public void setUser(Long userId){
        securityContext.set(userId);
    }

    public void removeUser(){
        securityContext.remove();
    }


    public Long getUser(){
        return securityContext.get();
    }







}

