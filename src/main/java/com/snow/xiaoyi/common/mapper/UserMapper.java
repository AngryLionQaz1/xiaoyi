package com.snow.xiaoyi.common.mapper;


import com.snow.xiaoyi.common.pojo.Authority;
import com.snow.xiaoyi.common.pojo.Role;
import com.snow.xiaoyi.common.pojo.User;
import org.apache.ibatis.annotations.*;
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

    String sql="select\n" +
            "        authority1_.id,\n" +
            "        authority1_.details,\n" +
            "        authority1_.name,\n" +
            "        authority1_.type,\n" +
            "        authority1_.type_name,\n" +
            "        authority1_.uri\n" +
            "    from\n" +
            "        s_role_authorities authoritie0_ \n" +
            "    inner join\n" +
            "        s_authority authority1_ \n" +
            "            on authoritie0_.authority_id=authority1_.id \n" +
            "    where\n" +
            "        authoritie0_.role_id=#{id};";
    @Select({"call authorities(#{roleId,mode=IN,jdbcType=INTEGER})"})
    @Options(statementType= StatementType.CALLABLE)
//    @Select(sql)
    List<Authority> authorities(@Param("roleId") Long id);


    String sql2="\t\t select\n" +
            "        role1_.id,\n" +
            "        role1_.code,\n" +
            "        role1_.name\n" +
            "    from\n" +
            "        s_user_roles roles0_ \n" +
            "    inner join\n" +
            "        s_role role1_ \n" +
            "            on roles0_.role_id=role1_.id \n" +
            "    where\n" +
            "        roles0_.user_id=#{id};";
    @Select({"call roles(#{userId,mode=IN,jdbcType=INTEGER})"})
    @Options(statementType= StatementType.CALLABLE)
//    @Select(sql2)
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "code", column = "code"),
            @Result(property = "authorities", column = "id", many=@Many(select = "com.snow.xiaoyi.common.mapper.UserMapper.authorities"))
    })
    List<Role> roles(@Param("userId")Long  id);


//    @Select({"call user(#{userId,mode=IN,jdbcType=INTEGER}"})
//    @Options(statementType= StatementType.CALLABLE)
//    @Results(value = {
//            @Result(id = true, property = "id", column = "id"),
//            @Result(property = "username", column = "username"),
//            @Result(property = "password", column = "password"),
//            @Result(property = "roles", column = "id", many=@Many(select = "com.snow.xiaoyi.common.mapper.UserMapper.roles"))
//    })
//    User user(@Param("userId")Long userId);

    @Select({"call user(#{userId,mode=IN,jdbcType=INTEGER})"})
    @Options(statementType= StatementType.CALLABLE)
//    @Select("select * from s_user where id=#{userId}")
    @Results(value = {
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "roles", column = "id", many=@Many(select = "com.snow.xiaoyi.common.mapper.UserMapper.roles"))
    })
    User user(@Param("userId")Long userId);







}
