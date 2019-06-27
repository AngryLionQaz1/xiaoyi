/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: Person
 * Author:   萧毅
 * Date:     2019/6/26 15:16
 * Description:
 */
package com.snow.xiaoyi.common.pojo;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("t_person")
public class Person {


    @Id       // 表示该字段为一个自增长的Id,注意,是数据库表中自增!!
    private int id; // @Id与属性名称id没有对应关系.

    @Name    // 表示该字段可以用来标识此对象，或者是字符型主键，或者是唯一性约束
    private String name;

    @Column      // 表示该对象属性可以映射到数据库里作为一个字段
    private int age;


}