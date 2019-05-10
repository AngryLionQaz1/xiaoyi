package com.snow.xiaoyi.common.mapper;

import com.snow.xiaoyi.common.pojo.Permissions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;


@Mapper
public interface BaseMapper {


    String sql="call Uri(#{pUri})";
    @Select({sql})
    @Options(statementType= StatementType.CALLABLE)
    Long uri(@Param("pUri") String pUri);


    String sql2="call Permissions(#{pUserId},#{pUri})";
    @Select({sql2})
    @Options(statementType= StatementType.CALLABLE)
    List<Map> permissions(@Param("pUserId") String pUserId, @Param("pUri") String pUri);


    String sql3="call Role(#{pUserId},#{pCode})";
    @Select({sql3})
    @Options(statementType= StatementType.CALLABLE)
    List<Map> role(@Param("pUserId") String pUserId, @Param("pCode") String pCode);



    @Select("select id from s_permissions where uri=#{uri}")
    Long findPermissionsByUri(@Param("uri") String uri);

    String sql_1="SELECT\n" +
            "\txs.id1_3_0_ AS userId,\n" +
            "\txs.uri10_0_4_ AS uri \n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\tuser0_.id AS id1_3_0_,\n" +
            "\troles1_.user_id AS user_id1_4_1_,\n" +
            "\trole2_.id AS role_id2_4_1_,\n" +
            "\trole2_.id AS id1_1_2_,\n" +
            "\trole2_.CODE AS code2_1_2_,\n" +
            "\trole2_.NAME AS name3_1_2_,\n" +
            "\tpermission3_.role_id AS role_id1_2_3_,\n" +
            "\tpermission4_.id AS permissi2_2_3_,\n" +
            "\tpermission4_.id AS id1_0_4_,\n" +
            "\tpermission4_.uri AS uri10_0_4_ \n" +
            "FROM\n" +
            "\ts_user user0_\n" +
            "\tLEFT OUTER JOIN s_user_roles roles1_ ON user0_.id = roles1_.user_id\n" +
            "\tLEFT OUTER JOIN s_role role2_ ON roles1_.role_id = role2_.id\n" +
            "\tLEFT OUTER JOIN s_role_permissions permission3_ ON role2_.id = permission3_.role_id\n" +
            "\tLEFT OUTER JOIN s_permissions permission4_ ON permission3_.permissions_id = permission4_.id \n" +
            "WHERE\n" +
            "\tuser0_.id = #{userId} \n" +
            "\t) AS xs \n" +
            "WHERE\n" +
            "\txs.id1_3_0_ = #{userId}\n" +
            "\tAND xs.uri10_0_4_ =#{uri}\n" +
            "\t\t";
    @Select(sql_1)
    List<Map> findPermissions(@Param("userId") String userId, @Param("uri") String uri);

    String sql2_2="SELECT\n" +
            "\txs.id1_3_0_ AS id,\n" +
            "\txs.code2_1_2_ AS CODE \n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\tuser0_.id AS id1_3_0_,\n" +
            "\tuser0_.create_time AS create_t2_3_0_,\n" +
            "\tuser0_.PASSWORD AS password3_3_0_,\n" +
            "\tuser0_.username AS username4_3_0_,\n" +
            "\troles1_.user_id AS user_id1_4_1_,\n" +
            "\trole2_.id AS role_id2_4_1_,\n" +
            "\trole2_.id AS id1_1_2_,\n" +
            "\trole2_.CODE AS code2_1_2_,\n" +
            "\trole2_.NAME AS name3_1_2_ \n" +
            "FROM\n" +
            "\ts_user user0_\n" +
            "\tLEFT OUTER JOIN s_user_roles roles1_ ON user0_.id = roles1_.user_id\n" +
            "\tLEFT OUTER JOIN s_role role2_ ON roles1_.role_id = role2_.id \n" +
            "WHERE\n" +
            "\tuser0_.id = #{userId}\n" +
            "\t) AS xs \n" +
            "WHERE\n" +
            "\txs.id1_3_0_ =#{userId} \n" +
            "\tAND xs.code2_1_2_ =#{code}";
    @Select(sql2_2)
    List<Map>findRole(@Param("userId") String userId, @Param("code") String code);


    String sql3_3="SELECT\n" +
            "\tax.p_id AS id,\n" +
            "\tax.p_name AS NAME,\n" +
            "\tax.p_code AS CODE,\n" +
            "\tax.p_p_code AS p_code,\n" +
            "\tax.p_p_name AS p_name,\n" +
            "\tax.p_uri AS uri,\n" +
            "\tax.p_details AS details,\n" +
            "\tax.p_flag AS flag,\n" +
            "\tax.p_if_menu AS if_menu,\n" +
            "\tax.p_m_order AS m_order \n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\tuser0_.id AS id1_3_0_,\n" +
            "\tpermission4_.id AS p_id,\n" +
            "\tpermission4_.NAME AS p_name,\n" +
            "\tpermission4_.CODE AS p_code,\n" +
            "\tpermission4_.p_code AS p_p_code,\n" +
            "\tpermission4_.p_name AS p_p_name,\n" +
            "\tpermission4_.uri AS p_uri,\n" +
            "\tpermission4_.details AS p_details,\n" +
            "\tpermission4_.flag AS p_flag,\n" +
            "\tpermission4_.if_menu AS p_if_menu,\n" +
            "\tpermission4_.m_order AS p_m_order \n" +
            "FROM\n" +
            "\ts_user user0_\n" +
            "\tLEFT OUTER JOIN s_user_roles roles1_ ON user0_.id = roles1_.user_id\n" +
            "\tLEFT OUTER JOIN s_role role2_ ON roles1_.role_id = role2_.id\n" +
            "\tLEFT OUTER JOIN s_role_permissions permission3_ ON role2_.id = permission3_.role_id\n" +
            "\tLEFT OUTER JOIN s_permissions permission4_ ON permission3_.permissions_id = permission4_.id \n" +
            "WHERE\n" +
            "\tuser0_.id = #{userId}\n" +
            "\t) AS ax";
    @Select(sql3_3)
    List<Permissions> userPermissions(@Param("userId") String userId);

}
