package com.snow.xiaoyi.config.mvc;

import com.snow.xiaoyi.common.bean.Config;
import com.snow.xiaoyi.config.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.Arrays;


@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {



    @Autowired
    private SecurityInterceptor securityInterceptor;
    @Autowired
    private Config config;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
//        InterceptorRegistration in=registry.addInterceptor(authorizationInterceptor);
//        setPath(config,in);

        registry.addInterceptor(securityInterceptor)
                .excludePathPatterns("/static/*")
                .excludePathPatterns("/error")
                .excludePathPatterns("/"+config.getFileUrl()+"/**")
                .addPathPatterns("/**");


    }


    /**
     * 获取路径
     * @param str
     * @return
     */
    public static String[] path(String str){
        return str.split(",");
    }


    /**
     * 设置路径
     * @param properties
     * @param http
     */
    public static void setPath(Config properties, InterceptorRegistration http) {
        String[] addPath=path(properties.getAddPath());
        String[] excludePath=path(properties.getExcludePath());
        Arrays.stream(addPath).forEach(i->http.addPathPatterns(i));
        Arrays.stream(excludePath).forEach(i->http.excludePathPatterns(i));
    }

      @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/"+config.getFileUrl()+"/**").addResourceLocations("file:"+ Paths.get(config.getFilePath())+"/");

    }



}

