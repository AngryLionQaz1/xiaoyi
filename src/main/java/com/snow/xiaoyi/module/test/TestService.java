/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: TestService
 * Author:   萧毅
 * Date:     2019/3/26 16:08
 * Description:
 */
package com.snow.xiaoyi.module.test;

import com.snow.xiaoyi.common.bean.Config;
import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.common.bean.Tips;
import com.snow.xiaoyi.common.pojo.Permissions;
import com.snow.xiaoyi.common.repository.PermissionsRepository;
import com.snow.xiaoyi.common.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TestService {


    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private Config config;
    @Autowired
    private PermissionsRepository permissionsRepository;

    /**不支持的-文件类型*/
    private static List<String> falseType=new ArrayList<>();


    public Result auths(){
        List<Permissions> all = permissionsRepository.findAll();
        return Result.success( auth(all));
    }

    public List<Permissions> auth(List<Permissions> list){
        //获取顶级
        //按group属性分组
        List<Permissions> authorities=permissionsRepository.findByFlag(true);
        List<Permissions> ax=new ArrayList<>();
        for (int i=0;i<authorities.size();i++){
            Permissions a=authorities.get(i);
            a.setPermissions(menuChild(a.getCode(),list));
            ax.add(a);
        }
        return ax;
    }

    public List<Permissions> menuChild(String code,List<Permissions> list){
        List<Permissions> lists = new ArrayList<>();
        for(Permissions a:list){
            if (a.getFlag())continue;
            if (a.getPCode().equals(code)){
                a.setPermissions(menuChild(a.getCode(),list));
                lists.add(a);
            }
        }
        return lists;
    }





    public Result uploadFile(MultipartFile file) {
        if (checkType(getType(file)))return Result.fail(Tips.TYPE_FALSE.msg);
        String path=fileUtils.saveFile(Paths.get(config.getFilePath()),file);
        if (path==null)return Result.fail();
        String url="https://"+config.getFileHost()+":"+config.getFilePort()+"/"+config.getFileUrl()+"/"+path;
        return Result.success(url,path);
    }


    /**判断该类型是否支持上传*/
    private boolean checkType(String type){
        if (falseType.size()==0)initType();
        return falseType.contains(type);
    }

    /**类型初始化*/
    private void initType(){
        String[] strings=config.getFileType().split(",");
        Arrays.stream(strings).forEach(i->falseType.add(i));
    }



    /**获取文件类型*/
    private String getType(MultipartFile file){
        String fileName = file.getOriginalFilename();
        //获取文件类型
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return suffix;
    }


    public Result path() {
        return Result.success("https://"+config.getFileHost()+":"+config.getFilePort()+"/"+config.getFileUrl()+"/");
    }










}