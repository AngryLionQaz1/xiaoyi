package com.snow.xiaoyi.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.snow.xiaoyi.common.bean.Config;
import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.common.mapper.BaseMapper;
import com.snow.xiaoyi.common.pojo.Role;
import com.snow.xiaoyi.common.pojo.User;
import com.snow.xiaoyi.common.util.JwtUtils;
import com.snow.xiaoyi.common.util.StringUtils;
import com.snow.xiaoyi.config.annotation.Security;
import com.snow.xiaoyi.config.annotation.SecurityPermission;
import com.snow.xiaoyi.config.security.SecurityContextHolder;
import com.snow.xiaoyi.config.token.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.snow.xiaoyi.common.bean.Result.auth;
import static com.snow.xiaoyi.common.bean.Result.over;


@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private Config config;
    @Resource
    private BaseMapper baseMapper;
    @Autowired
    private SecurityContextHolder securityContextHolder;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) return true;
        if (hasPermission(request,response,handler))return true;
        return false;
    }

    @Override
    //整个请求执行完成后调用
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        securityContextHolder.removeUser();
    }

    private boolean hasPermission(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = request.getHeader(config.getAuthorization());
        //获取类上的注解
        Security requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Security.class);
        // 获取方法上的注解
        if (requiredPermission == null) requiredPermission = handlerMethod.getMethod().getAnnotation(Security.class);
        if (requiredPermission == null) return true;
        if (!"".equals(requiredPermission.value()) && permission(request.getRequestURI(), requiredPermission.value()))
            return true;
        if (Optional.ofNullable(token).isPresent()) {
            Result result = checkToken(token, request.getRequestURI());
            if (result.getCode() != 1) {
                response(response, result);
                return false;
            }
            setUser(Long.valueOf(result.getData().toString()));
            return true;
        } else {
            response(response, over());
            return false;
        }
    }

    private void setUser(Long userId) {
        securityContextHolder.setUser(userId);
    }

    private Result checkToken(String token,String uri) {
        String userId = JwtUtils.getInfoFromToken(token, config.getJwtSecretKey());
        if (userId==null)return Result.over();
        if (StringUtils.isNull(uri)) return Result.success(userId);
        if (baseMapper.findRole(userId, config.getAuthorityAdmin()).size()>0) return Result.success(userId);
        if (baseMapper.findPermissions(userId,uri).size()==0)return Result.auth();
        return Result.success(userId);
    }

    private boolean permission(String uri,String value){
        boolean flag=false;
        String[]s = value.split(",");
        Set<String> strings=new LinkedHashSet<>();
        Arrays.stream(s).forEach((v)->strings.add(v));
        String[] ss=uri.split("/");
        for (int i=0;i<ss.length;i++){
            if (strings.contains(ss[i])){
                flag=true;
                break;
            }
        }
        return flag;
    }

    public void response(HttpServletResponse response, Result result){
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter out= null;
        try {
            out = response.getWriter();
            out.write(JSON.toJSONString(result));
            out.flush();
        } catch (IOException e) {
        }finally {
            out.close();
        }

    }

}
