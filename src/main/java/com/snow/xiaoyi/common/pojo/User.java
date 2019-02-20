package com.snow.xiaoyi.common.pojo;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Builder
@Table(name = "s_user",uniqueConstraints = {@UniqueConstraint(columnNames = "username")},indexes = {@Index(columnList = "username")})
@AllArgsConstructor
@NoArgsConstructor
/**
 *  @Procedure存储过程查询方法
 *  调用数据库存储过程需要在实体类定义定义
 * name: 在EntityManager中的名字 NamedStoredProcedureQuery使用
 * procedureName: 数据库里存储过程的名字
 * parameters: 使用IN/OUT参数
 *
 * 存储过程使用了注解@NamedStoredProcedureQuery 并绑定到一个JPA表。
 * procedureName是存储过程的名字
 * name是JPA中存储过程的名字
 * 使用注解@StoredProcedureParameter来定义存储过程使用的IN/OU参书
 */

@NamedStoredProcedureQuery(name = "User.plusl",procedureName = "pluslinout",parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN,name = "arg",type=Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT,name="res",type = Integer.class)
})
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @ManyToMany(targetEntity = Role.class,fetch = FetchType.LAZY)
    @JoinTable(joinColumns={@JoinColumn(name="user_id")}, inverseJoinColumns={@JoinColumn(name="role_id")})
    @JSONField(serialize = false)
    private List<Role> roles=new ArrayList<>();
    @Transient
    private String token;

}

