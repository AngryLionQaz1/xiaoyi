/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: SecurityController
 * Author:   萧毅
 * Date:     2019/3/21 16:10
 * Description:
 */
package com.snow.xiaoyi.module.security;


import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.config.annotation.Security;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "权限")
@RestController
@RequestMapping("security")
@Security(value = "sx",order = 1,names = "权限一级菜单")
public class SecurityController {



    @GetMapping("s2")
    @ApiOperation(value = "二级权限")
    public Result s2(){

        return Result.success();
    }




    @GetMapping("s3")
    @ApiOperation(value = "三级权限")
    @Security(menu = 3,sign = 9,names = "权限二级级菜单")
    public Result s3(){


        return Result.success();
    }


    @GetMapping("s3_2")
    @ApiOperation(value = "三级权限2")
    @Security(menu = 3,sign = 9,names = "权限二级级菜单2")
    public Result s3_2(){


        return Result.success();
    }
    @GetMapping("s3_21")
    @ApiOperation(value = "三级权限21")
    @Security(menu = 3,sign = 8,names = "权限二级级菜单2")
    public Result s3_21(){


        return Result.success();
    }

    @GetMapping("s4")
    @ApiOperation(value = "四级权限")
    @Security(menu = 4,sign = 8,names = "权限二级级菜单,权限三级菜单")
    public Result s4(){

        return Result.success();
    }


    @GetMapping("s42")
    @ApiOperation(value = "四级权限2")
    @Security(menu = 4,sign = 8,names = "权限二级级菜单,权限三级菜单")
    public Result s5(){

        return Result.success();
    }

    @GetMapping("s6")
    @ApiOperation(value = "四级权限2")
    @Security(menu = 4,sign = 9,names = "权限二级级菜单,权限三级菜单")
    public Result s6(){

        return Result.success();
    }


    @GetMapping("sx")
    @ApiOperation(value = "权限排除项")
    public Result sx(){

        return Result.success();
    }













}