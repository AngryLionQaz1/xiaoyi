package com.snow.xiaoyi.config.init;

import com.snow.xiaoyi.common.bean.Config;
import com.snow.xiaoyi.common.pojo.Authority;
import com.snow.xiaoyi.common.repository.AuthorityRepository;
import com.snow.xiaoyi.config.annotation.Auth;
import com.snow.xiaoyi.config.annotation.AuthS;
import com.snow.xiaoyi.config.annotation.AuthX;
import com.snow.xiaoyi.config.annotation.Security;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AppInit2 implements ApplicationRunner {

    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private Config config;

    @Override
    public void run(ApplicationArguments args) {

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo,HandlerMethod> map = mapping.getHandlerMethods();
        List<Authority> authorities = new ArrayList<>();
        int count=0;
        for (RequestMappingInfo info : map.keySet()){
            Method m = map.get(info).getMethod();
            List<Authority> list=menu(count,info,m);
            list.forEach(a->{


                //设置url
                url(a,info,m);
                //设置ApiOperation 基本信息
                apiOperation(a,info,m);

            });








        }

    }

    /**
     * 获取菜单
     */
    public List<Authority> menu(Integer count,RequestMappingInfo info, Method m){

        //获取类上security
        Security clzss=m.getDeclaringClass().getAnnotation(Security.class);
        if (clzss==null)return null;
        List<Authority> list=new ArrayList<>();







        return new ArrayList<>();

    }


    /**
     * 普通菜单
     */
    public Authority menus(Security security){
        return Authority
                .builder()
                .pName("")
                .pCode("")
                .code(String.valueOf(security.order()))
                .name(security.names().split(",")[0])
                .order(security.order())
                .ifMenu(true)
                .flag(true)
                .build();
    }


    /**
     * 顶级菜单
     */
    public Authority menuTop(Security security){
       return Authority
               .builder()
               .pName("")
               .pCode("")
               .code(String.valueOf(security.order()))
               .name(security.names().split(",")[0])
               .order(security.order())
               .ifMenu(true)
               .flag(true)
               .build();
    }



    /**
     * 获取url
     */
    public void url(Authority a,RequestMappingInfo info, Method m ){
        //获取url的Set集合，一个方法可能对应多个url
        Set<String> patterns = info.getPatternsCondition().getPatterns();
        for (String url : patterns)a.setUri(url);
    }

    /**
     * 获取ApiOperation 基本信息
     */
    public void apiOperation(Authority a,RequestMappingInfo info, Method m ){
        ApiOperation apiOperation = m.getAnnotation(ApiOperation.class);
        Method[] me = {};
        if(apiOperation!=null) me = apiOperation.annotationType().getDeclaredMethods();
        for(Method meth : me){
            try {
                if("notes".equals(meth.getName())){
                    String color = (String) meth.invoke(apiOperation, new  Object[]{});
                    a.setDetails(color);
                }
                if("value".equals(meth.getName())){
                    String color = (String) meth.invoke(apiOperation, new  Object[]{});
                    a.setName(color);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

