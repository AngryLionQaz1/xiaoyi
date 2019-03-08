/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: FileController
 * Author:   萧毅
 * Date:     2019/3/8 15:19
 * Description:
 */
package com.snow.xiaoyi.module.file;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = "文件")
public class FileController {


    @Autowired
    private FileService fileService;

    @GetMapping("download")
    @ApiOperation(value = "下载")
    public void download(HttpServletRequest request, HttpServletResponse response){
        try {
            fileService.download(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("syncDownload")
    @ApiOperation(value = "异步下载")
    public void syncDownload(HttpServletRequest request, HttpServletResponse response){

        fileService.syncDownload(request,response);


    }











}