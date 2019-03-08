package com.snow.xiaoyi.config.init;

import com.snow.xiaoyi.common.bean.Config;
import com.snow.xiaoyi.common.pojo.Authority;
import com.snow.xiaoyi.common.repository.AuthorityRepository;
import com.snow.xiaoyi.config.annotation.Auth;
import com.snow.xiaoyi.config.annotation.AuthS;
import com.snow.xiaoyi.config.annotation.AuthX;
import com.snow.xiaoyi.config.annotation.AuthorityType;
import io.swagger.annotations.Api;
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
public class AppInit implements ApplicationRunner {

    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private Config config;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (config.getAuthorityInit())new Thread(()->initData(getUrl())).run();
    }


    /**
     * 查看数据库中是否存在
     * @param url
     * @return
     */
     private boolean checkData(String url){
         return authorityRepository.findByUri(url).isPresent();
     }

     private List<Authority> makeData(List<Map<String,String>> urls){
       return   urls.stream()
                 .filter(Objects::nonNull)
                 .filter(i->!checkData(i.get("url")))
                 .map(i->makeData(i))
                 .collect(Collectors.toList());
     }

    /**
     * 生成数据
     * @param map
     * @return
     */
    private Authority makeData(Map<String,String> map){
        Authority a=Authority.builder().build();
        if (map.containsKey("name"))a.setName(map.get("name"));
        if (map.containsKey("code"))a.setCode(map.get("code"));
        if (map.containsKey("pCode"))a.setPCode(map.get("pCode"));
        if (map.containsKey("url"))a.setUri(map.get("url"));
        if (map.containsKey("details"))a.setDetails(map.get("details"));
        if (map.containsKey("pName"))a.setPName(map.get("pName"));
        a.setFlag(false);
        a.setIfMenu(false);
        return a;
    }
    
    /**
     * 数据库
     * @param urls
     */
    private void initData(List<Map<String,String>> urls){
        authorityRepository.saveAll(makeData(urls));
    }

    /**
     * 获取url
     * @return
     */
    private List<Map<String,String>> getUrl(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo,HandlerMethod> map = mapping.getHandlerMethods();
        List<Map<String,String>> urlList = new ArrayList<>();
        for (RequestMappingInfo info : map.keySet()){
            urlList.add(makeAuthority(info,map));
        }
        return urlList;
    }

    /***
     * 获取权限信息
     * @param info
     * @param map
     * @return
     */
    public  Map<String,String> makeAuthority(RequestMappingInfo info,  Map<RequestMappingInfo,HandlerMethod> map){
        HandlerMethod hm = map.get(info);
        Method m = hm.getMethod();
        Map<String,String> limitMap=auth(m);
        if (limitMap==null)return null;
        //获取url的Set集合，一个方法可能对应多个url
        Set<String> patterns = info.getPatternsCondition().getPatterns();
        for (String url : patterns) limitMap.put("url",url);
        getAuthority(m,limitMap);
        return limitMap;
    }

    /**
     * 权限分类
     */
    public Map<String,String> auth(Method m){
        //获取方法上的注解
        Auth a=m.getAnnotation(Auth.class);
        if (a==null)return null;
        //获取类上注解
        AuthX ax=m.getDeclaringClass().getAnnotation(AuthX.class);
        if (ax==null)return null;
        Map<String,String> map=new HashMap<>();
        map.put("code",String.valueOf(a.value()));
        map.put("pCode",String.valueOf(ax.value()));
        map.put("pName",String.valueOf(ax.name()));
        initFlag(ax);
        AuthX ax2=m.getAnnotation(AuthX.class);
        if (ax2!=null){
            initFlag(String.valueOf(ax.value()),ax2);
            map.put("pCode",String.valueOf(ax2.value()));
            map.put("pName",String.valueOf(ax2.name()));
        }
        AuthS ax3=m.getAnnotation(AuthS.class);
        if (ax3!=null){
            initFlag(String.valueOf(ax2.value()),ax3);
            map.put("pCode",String.valueOf(ax3.value()));
            map.put("pName",String.valueOf(ax3.name()));
        }
        return map;
    }


    /***
     * 菜单初始化
     */
    public void initFlag(AuthX authX){
        Optional<Authority> byCode = authorityRepository.findByCode(String.valueOf(authX.value()));
        if (byCode.isPresent())return;
        Authority build = Authority.builder()
                .name(authX.name())
                .code(String.valueOf(authX.value()))
                .flag(authX.flag())
                .pCode(String.valueOf(authX.value()))
                .ifMenu(true)
                .build();
        authorityRepository.save(build);
    }
    public void initFlag(String pCode, AuthX authX){
        Optional<Authority> byCode = authorityRepository.findByCode(String.valueOf(authX.value()));
        if (byCode.isPresent())return;
        Authority build = Authority.builder()
                .name(authX.name())
                .code(String.valueOf(authX.value()))
                .flag(authX.flag())
                .pCode(pCode)
                .ifMenu(true)
                .build();
        authorityRepository.save(build);
    }
    public void initFlag(String pCode, AuthS authS){
        Optional<Authority> byCode = authorityRepository.findByCode(String.valueOf(authS.value()));
        if (byCode.isPresent())return;
        Authority build = Authority.builder()
                .name(authS.name())
                .code(String.valueOf(authS.value()))
                .flag(authS.flag())
                .pCode(pCode)
                .ifMenu(true)
                .build();
        authorityRepository.save(build);
    }

    /**
     * 获取参数信息
     * @param m
     * @param limitMap
     */
    public void getAuthority(Method m,Map<String,String> limitMap){
        ApiOperation apiOperation = m.getAnnotation(ApiOperation.class);
        Method[] me = {};
        if(apiOperation!=null) me = apiOperation.annotationType().getDeclaredMethods();
        for(Method meth : me){
            try {
                if("notes".equals(meth.getName())){
                    String color = (String) meth.invoke(apiOperation, new  Object[]{});
                    limitMap.put("details",color);
                }
                if("value".equals(meth.getName())){
                    String color = (String) meth.invoke(apiOperation, new  Object[]{});
                    limitMap.put("name",color);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

