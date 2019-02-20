package com.snow.xiaoyi.config.init;

import com.snow.xiaoyi.common.bean.Config;
import com.snow.xiaoyi.common.pojo.Authority;
import com.snow.xiaoyi.common.repository.AuthorityRepository;
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
        if (map.containsKey("url"))a.setUri(map.get("url"));
        if (map.containsKey("details"))a.setDetails(map.get("details"));
        if (map.containsKey("typeName"))a.setTypeName(map.get("typeName"));
        if (map.containsKey("type"))a.setType(Integer.valueOf(map.get("type")));
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
        Integer code=getAuthorityType(m);
        if (code==config.getAuthorityType())return null;
        Map<String,String> limitMap = new HashMap<>();
        //获取url的Set集合，一个方法可能对应多个url
        Set<String> patterns = info.getPatternsCondition().getPatterns();
        for (String url : patterns) limitMap.put("url",url);
        getAuthority(m,limitMap,code);
        return limitMap;
    }

    /**
     * 获取type类型
     * @param m
     * @return
     */
    public Integer getAuthorityType(Method m){
        // 获取方法上的注解
        AuthorityType authorityType=m.getAnnotation(AuthorityType.class);
        //获取类上的注解
        if (authorityType==null)authorityType=m.getDeclaringClass().getAnnotation(AuthorityType.class);
        if (authorityType==null)return config.getAuthorityType();
        return authorityType.code();
    }

    /**
     * 获取参数信息
     * @param m
     * @param limitMap
     * @param type
     */
    public void getAuthority(Method m,Map<String,String> limitMap,Integer type){
        limitMap.put("type",String.valueOf(type));
        Api api=m.getDeclaringClass().getAnnotation(Api.class);
        if (api!=null)limitMap.put("typeName",api.tags()[0]);
        if (api==null)limitMap.put("typeName",config.getAuthorityTypeName());
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

