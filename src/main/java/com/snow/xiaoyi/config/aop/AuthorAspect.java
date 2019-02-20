package com.snow.xiaoyi.config.aop;

import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.common.bean.Tips;
import com.snow.xiaoyi.common.pojo.User;
import com.snow.xiaoyi.config.annotation.Author;
import com.snow.xiaoyi.config.security.SecurityContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Component
public class AuthorAspect {



    @Autowired
    private SecurityContextHolder securityContextHolder;
    private Map<String,String> roleMap=new HashMap<>();

    @Pointcut(value = "@annotation(com.snow.xiaoyi.config.annotation.Author)")
    public void aspect(){

    }

    /**
     * 在调用通知方法之前和之后运行通知。
     * @param joinPoint
     * @return
     */
    @Around(value = "aspect()")
    public Object around(ProceedingJoinPoint joinPoint){
        Author annotation=((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(Author.class);
        if (!getUser()) return Result.fail(Tips.USER_NOT.msg);
        try {
            if ("".equals(annotation.value())) return joinPoint.proceed();
            if (checkRole(annotation.value()))return joinPoint.proceed();
            return Result.auth();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Result.fail(Tips.USER_NOT.msg);
    }


    /**
     * 获取用户信息
     *
     */
    public boolean getUser(){
        User user=securityContextHolder.getUser();
        if (user!=null) return true;
        return true;
    }


    private boolean checkRole(String str){
        Map<String,String> rx=getRole(str);
        List<String> roles=securityContextHolder
                .getUser()
                .getRoles()
                .stream()
                .map(i->i.getName())
                .collect(Collectors.toList());
        for (int i=0;i<roles.size();i++){
            if (rx.containsKey(String.valueOf(roles.get(i)))){
                return true;
            }
        }
        return false;
    }

    private Map<String,String> getRole(String str){
        roleMap.clear();
        Arrays.stream(str.split(",")).forEach(i->roleMap.put(i,i));
        return roleMap;
    }




}

