package com.snow.xiaoyi.common.mapper;


import com.snow.xiaoyi.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from s_user")
    List<User> findAll();

    @Select({
            "call pluslinout(#{arg,mode=IN,jdbcType=INTEGER}," +
                    "#{res,mode=OUT,jdbcType=INTEGER})"
    })
    @Options(statementType= StatementType.CALLABLE)
    void testx(Map<String,Integer> map);


    @Select({
            "call findById(#{iid,mode=IN,jdbcType=INTEGER})"
    })
    User findById(Map<String,Integer> map);




}
