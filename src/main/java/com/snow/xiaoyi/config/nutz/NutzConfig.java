/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: NutzConfig
 * Author:   萧毅
 * Date:     2019/6/26 15:12
 * Description:
 */
package com.snow.xiaoyi.config.nutz;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class NutzConfig {




    @Bean
    public Dao dao(DataSource dataSource){
      return new NutDao(dataSource);
    }














}