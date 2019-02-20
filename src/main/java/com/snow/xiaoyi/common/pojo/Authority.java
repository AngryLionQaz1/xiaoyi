package com.snow.xiaoyi.common.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Entity
@Data
@Table(name = "s_authority")
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**权限名*/
    private String name;
    /**类别*/
    private Integer type;
    /**类别名*/
    private String typeName;
    /**权限uri*/
    private String uri;
    /**详细描述*/
    private String details;
    @ManyToMany(cascade=CascadeType.REFRESH,mappedBy="authorities",fetch = FetchType.EAGER)
    @JSONField(serialize = false)
    private List<Role> roles;











}

