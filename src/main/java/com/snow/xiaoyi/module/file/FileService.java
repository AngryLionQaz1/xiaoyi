/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: FileService
 * Author:   萧毅
 * Date:     2019/3/8 15:19
 * Description:
 */
package com.snow.xiaoyi.module.file;

import com.snow.xiaoyi.common.util.FileUtil;
import com.snow.xiaoyi.common.util.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URLDecoder;

@Service
@Slf4j
public class FileService {

    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {

        File downloadFile = new File("F:\\工具\\软件2\\python-3.6.5-amd64.exe");//要下载的文件
        long pos = FileUtil.headerSetting(downloadFile, request, response);
//      log.info("跳过"+pos);
        ServletOutputStream os = null;
        BufferedOutputStream out = null;
        RandomAccessFile raf = null;
        byte b[] = new byte[1024];//暂存容器
        try {
            os = response.getOutputStream();
            out = new BufferedOutputStream(os);
            raf = new RandomAccessFile(downloadFile, "r");
            raf.seek(pos);
            try {
                int n = 0;
                while ((n = raf.read(b, 0, 1024)) != -1) {
                    out.write(b, 0, n);
                }
                out.flush();
            } catch(IOException ie) {
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (out!= null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }



    }



    private File getFile(HttpServletRequest request) throws Exception {
        String uriStr=request.getParameter("uri");
        if (null==uriStr)return null;
        uriStr= URLDecoder.decode(uriStr,"UTF-8");
        if(uriStr.startsWith("file://")){
            return new File(uriStr.substring(7));
        }else if (uriStr.startsWith("hbase://")){
            return new File(new URI(uriStr));
        }
        return null;
    }


    public void syncDownload(HttpServletRequest request, HttpServletResponse response) {


        File downloadFile = new File("F:\\工具\\软件2\\python-3.6.5-amd64.exe");//要下载的文件
        long fileLength = downloadFile.length();//记录文件大小
        long pastLength = 0;//记录已下载文件大小
        int rangeSwitch = 0;//0：从头开始的全文下载；1：从某字节开始的下载（bytes=27000-）；2：从某字节开始到某字节结束的下载（bytes=27000-39000）
        long toLength = 0;//记录客户端需要下载的字节段的最后一个字节偏移量（比如bytes=27000-39000，则这个值是为39000）
        long contentLength = 0;//客户端请求的字节总量
        String rangeBytes = "";//记录客户端传来的形如“bytes=27000-”或者“bytes=27000-39000”的内容
        RandomAccessFile raf = null;//负责读取数据
        OutputStream os = null;//写出数据
        OutputStream out = null;//缓冲
        byte b[] = new byte[1024];//暂存容器

        if (request.getHeader("Range") != null) {// 客户端请求的下载的文件块的开始字节
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            log.info("request.getHeader(\"Range\")=" + request.getHeader("Range"));
            rangeBytes = request.getHeader("Range").replaceAll("bytes=", "");
            if (rangeBytes.indexOf('-') == rangeBytes.length() - 1) {//bytes=969998336-
                rangeSwitch = 1;
                rangeBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
                pastLength = Long.parseLong(rangeBytes.trim());
                contentLength = fileLength - pastLength + 1;//客户端请求的是 969998336 之后的字节
            } else {//bytes=1275856879-1275877358
                rangeSwitch = 2;
                String temp0 = rangeBytes.substring(0, rangeBytes.indexOf('-'));
                String temp2 = rangeBytes.substring(rangeBytes.indexOf('-') + 1, rangeBytes.length());
                pastLength = Long.parseLong(temp0.trim());//bytes=1275856879-1275877358，从第 1275856879 个字节开始下载
                toLength = Long.parseLong(temp2);//bytes=1275856879-1275877358，到第 1275877358 个字节结束
                contentLength = toLength - pastLength + 1;//客户端请求的是 1275856879-1275877358  之间的字节
            }
        } else {//从开始进行下载
            contentLength = fileLength;//客户端要求全文下载
        }

        /**
         * 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
         * 响应的格式是:
         * Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
         * ServletActionContext.getResponse().setHeader("Content-Length",
         * new Long(file.length() - p).toString());
         */
        response.reset();//告诉客户端允许断点续传多线程连接下载,响应的格式是:Accept-Ranges: bytes
        response.setHeader("Accept-Ranges", "bytes");//如果是第一次下,还没有断点续传,状态是默认的 200,无需显式设置;响应的格式是:HTTP/1.1 200 OK
        if (pastLength != 0) {
            //不是从最开始下载,
            //响应的格式是:
            //Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
            log.info("----------------------------不是从开始进行下载！服务器即将开始断点续传...");
            switch (rangeSwitch) {
                case 1: {//针对 bytes=27000- 的请求
                    String contentRange = new StringBuffer("bytes ").append(new Long(pastLength).toString()).append("-").append(new Long(fileLength - 1).toString()).append("/").append(new Long(fileLength).toString()).toString();
                    response.setHeader("Content-Range", contentRange);
                    break;
                }
                case 2: {//针对 bytes=27000-39000 的请求
                    String contentRange = rangeBytes + "/" + new Long(fileLength).toString();
                    response.setHeader("Content-Range", contentRange);
                    break;
                }
                default: {
                    break;
                }
            }
        } else {
            //是从开始下载
            log.info("----------------------------是从开始进行下载！");
        }
        try {
                  response.addHeader("Content-Disposition", "attachment; filename=\"" + downloadFile.getName() + "\"");
                 response.setContentType( IoUtil.setContentType(downloadFile.getName()));// set the MIME type.
                 response.addHeader("Content-Length", String.valueOf(contentLength));
                os = response.getOutputStream();
                    out = new BufferedOutputStream(os);
                     raf = new RandomAccessFile(downloadFile, "r");
                    try {
                          switch (rangeSwitch) {
                                    case 0 : {//普通下载，或者从头开始的下载
                                             //同1
                                      }
                              case 1 : {//针对 bytes=27000- 的请求
                                        raf.seek(pastLength);//形如 bytes=969998336- 的客户端请求，跳过 969998336  个字节
                                            int n = 0;
                                        while ((n = raf.read(b, 0, 1024)) != -1) {
                                                   out.write(b, 0, n);
                                                 }
                                              break;
                                          }
                                 case 2 : {//针对 bytes=27000-39000 的请求
                                           raf.seek(pastLength - 1);//形如 bytes=1275856879-1275877358 的客户端请求，找到第 1275856879 个字节
                                         int n = 0;
                                           long readLength = 0;//记录已读字节数
                                          while (readLength <= contentLength - 1024) {//大部分字节在这里读取
                                                    n = raf.read(b, 0, 1024);
                                                   readLength += 1024;
                                                    out.write(b, 0, n);
                                                 }
                                             if (readLength <= contentLength) {//余下的不足 1024 个字节在这里读取
                                                    n = raf.read(b, 0, (int)(contentLength - readLength));
                                                   out.write(b, 0, n);
                                                   }
                        //
                       //                      raf.seek(pastLength);//形如 bytes=1275856879-1275877358 的客户端请求，找到第 1275856879 个字节
                        //                      while (raf.getFilePointer() < toLength) {
                        //                          out.write(raf.read());
                        //                      }
                                              break;
                                      }
                                   default : {
                                            break;
                                          }
                                  }
                            out.flush();
                         } catch(IOException ie) {
                            /**
                                * 在写数据的时候，
                               * 对于 ClientAbortException 之类的异常，
                                * 是因为客户端取消了下载，而服务器端继续向浏览器写入数据时，
                            * 抛出这个异常，这个是正常的。
                               * 尤其是对于迅雷这种吸血的客户端软件，
                                * 明明已经有一个线程在读取 bytes=1275856879-1275877358，
                               * 如果短时间内没有读取完毕，迅雷会再启第二个、第三个。。。线程来读取相同的字节段，
                                  * 直到有一个线程读取完毕，迅雷会 KILL 掉其他正在下载同一字节段的线程，
                                 * 强行中止字节读出，造成服务器抛 ClientAbortException。
                                 * 所以，我们忽略这种异常
                                */
                             //ignore
                           }
                   } catch (Exception e) {
                       log.error(e.getMessage(), e);
                 }
        }
}