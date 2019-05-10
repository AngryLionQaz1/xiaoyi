/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: TestController
 * Author:   萧毅
 * Date:     2019/3/26 14:51
 * Description:
 */
package com.snow.xiaoyi.module.test;


import com.snow.xiaoyi.common.bean.Config;
import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.common.pojo.Permissions;
import com.snow.xiaoyi.common.pojo.Role;
import com.snow.xiaoyi.common.pojo.User;
import com.snow.xiaoyi.common.repository.PermissionsRepository;
import com.snow.xiaoyi.common.repository.RoleRepository;
import com.snow.xiaoyi.common.repository.UserRepository;
import com.snow.xiaoyi.common.util.JwtUtils;
import com.snow.xiaoyi.common.util.PasswordEncoderUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Api(tags = "测试")
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionsRepository permissionsRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TestService testService;
    @Autowired
    private Config config;


    @GetMapping("password")
    @ApiOperation(value = "密码")
    public Result password(@RequestParam String password){
        return Result.success(PasswordEncoderUtils.encode(password));
    }


    @GetMapping("auths")
    @ApiOperation(value = "获取所有")
    public Result auths(){
       return testService.auths();
    }

    @GetMapping("allUsers")
    @ApiOperation(value = "获取所有用户")
    public Result allUsers(){
        return Result.success(userRepository.findAll());
    }

    @PostMapping("addRole")
    @ApiOperation(value = "添加角色")
    public Result addRole(@ApiParam(value = "权限ID",required =true)@RequestParam String ids,
                          @ApiParam(value = "角色名称",required = true)@RequestParam String name,
                          @ApiParam(value = "角色标识",required = true)@RequestParam String code){
        Optional<Role> byName = roleRepository.findByName(name);
        if (byName.isPresent())return Result.fail();
        List<Permissions>list=new ArrayList<>();
        Arrays.stream(ids.split(","))
                .forEach(i->{
                    Optional<Permissions> byId = permissionsRepository.findById(Long.valueOf(i));
                    if (byId.isPresent())list.add(byId.get());
                });
        if (list.size()==0)return Result.fail();
        Role role= Role.builder()
                .name(name)
                .code(code)
                .permissions(list)
                .build();
        return Result.success(roleRepository.save(role));
    }
    @PostMapping("addUser")
    @ApiOperation(value = "添加用户")
    public Result addUser(@ApiParam(value = "角色ID",required =true)@RequestParam String ids,
                          @ApiParam(value = "用户名",required = true)@RequestParam String name,
                          @ApiParam(value = "密码",required = true)@RequestParam String password){
        Optional<User> byName = userRepository.findByUsername(name);
        if (byName.isPresent())return Result.fail();
        List<Role>list=new ArrayList<>();
        Arrays.stream(ids.split(","))
                .forEach(i->{
                    Optional<Role> byId = roleRepository.findById(Long.valueOf(i));
                    if (byId.isPresent())list.add(byId.get());
                });
        if (list.size()==0)return Result.fail();
        User user = User.builder()
                .username(name)
                .password(PasswordEncoderUtils.encode(password))
                .createTime(LocalDateTime.now())
                .roles(list)
                .build();
        return Result.success(userRepository.save(user));
    }

    @PostMapping("delUserRole")
    @ApiOperation(value = "删除用户角色")
    public Result delUserRole(@ApiParam(value = "角色ID",required = true)@RequestParam Long id,
                              @ApiParam(value = "用户ID",required = true)@RequestParam Long userId){
        Optional<Role> o=roleRepository.findById(id);
        if (!o.isPresent())return Result.fail();
        Optional<User> r=userRepository.findById(userId);
        if (!r.isPresent())return Result.fail();
        User user=r.get();
        List<Role> roles=user.getRoles();
        roles.remove(o.get());
        return Result.success(userRepository.save(user));


    }

    @GetMapping("ssx")
    @ApiOperation(value = "测试")
    public Result test(){
        return  Result.success(JwtUtils.generateToken("1",config.getJwtSecretKey(),config.getJwtTokenValidity()));
    }

    @GetMapping("path")
    @ApiOperation(value = "获取地址")
    public Result path(){
        return  Result.success(testService.path());
    }

    @PostMapping("upload")
    @ApiOperation(value = "上传文件")
    public Result uploadFle(@RequestParam MultipartFile file){
        return  Result.success(testService.uploadFile(file));
    }






}