/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: FileUtil
 * Author:   萧毅
 * Date:     2019/3/8 15:44
 * Description:
 */
package com.snow.xiaoyi.common.util;


import com.snow.xiaoyi.common.bean.RangeSettings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class FileUtil {


    public static long headerSetting(File file, HttpServletRequest request, HttpServletResponse response){

        long len=file.length();
        if (null==request.getHeader("Range")){
            setResponse(RangeSettings.builder().totalLength(len).build(),file.getName(),response);
             return 0;
        }
        String range=request.getHeader("Range").replaceAll("bytes=","");
        RangeSettings settings=getSetting(len,range);
        setResponse(settings,file.getName(),response);
        return settings.getStart();


    }

    private static RangeSettings getSetting(long len, String range) {
        long contentLength = 0;
        long start = 0;
        long end = 0;
        if (range.startsWith("-")){
            contentLength = Long.parseLong(range.substring(1));//要下载的量
            end = len-1;
            start = len - contentLength;
        }else if(range.endsWith("-")){
            start = Long.parseLong(range.replace("-", ""));
            end = len -1;
            contentLength = len - start;
        }else {
            String[] se = range.split("-");
            start = Long.parseLong(se[0]);
            end = Long.parseLong(se[1]);
            contentLength = end-start+1;
        }
        return RangeSettings.builder().start(start).end(end).contentLength(contentLength).totalLength(len).build();
    }

    public static void setResponse(RangeSettings settings, String fileName, HttpServletResponse response){

        response.addHeader("Content-Disposition", "attachment; filename=\"" + IoUtil.toUtf8String(fileName) + "\"");
        response.setContentType( IoUtil.setContentType(fileName));
        if (!settings.isRange()){
            response.addHeader("Content-Length", String.valueOf(settings.getTotalLength()));
        }else{
            long start = settings.getStart();
            long end = settings.getEnd();
            long contentLength = settings.getContentLength();
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.addHeader("Content-Length", String.valueOf(contentLength));
            String contentRange = new StringBuffer("bytes ").append(start).append("-").append(end).append("/").append(settings.getTotalLength()).toString();
            response.setHeader("Content-Range", contentRange);
        }


    }



















}