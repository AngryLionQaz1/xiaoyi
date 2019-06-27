/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: NutzController
 * Author:   萧毅
 * Date:     2019/6/26 15:15
 * Description:
 */
package com.snow.xiaoyi.module.nutz;

import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.common.pojo.Person;
import com.snow.xiaoyi.common.pojo.User;
import io.swagger.annotations.ApiOperation;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("nutz")
public class NutzController {

    @Autowired
    private Dao dao;


    @PostMapping("create")
    @ApiOperation(value = "创建表")
    public Result create(){

        Entity<Person> personEntity = dao.create(Person.class, false);

        return Result.success();
    }


    @PostMapping("insert")
    @ApiOperation(value = "插入表")
    public Result insert(){
        Person p = new Person();
        p.setName("ABC");
        p.setAge(20);
        Person insert = dao.insert(p);
        System.out.println(p.getId());
        return Result.success(insert);
    }

    @GetMapping("fetch")
    @ApiOperation(value = "查询")
    public Result fetch(){

        Record record=dao.fetch("s_user", Cnd.where("id","=",1));

        System.out.println(record.getString("username"));

        User user = record.toPojo(User.class);

        Sql sql = Sqls.create("select * from s_user where username=@name");
        sql.setParam("name","1");
        System.out.println(sql);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(dao.getEntity(User.class));
        List<User> list = dao.execute(sql).getList(User.class);
        System.out.println(list.size());
        return Result.success(list);


    }








}