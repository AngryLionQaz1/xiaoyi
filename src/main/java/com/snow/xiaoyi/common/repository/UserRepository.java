package com.snow.xiaoyi.common.repository;
import com.snow.xiaoyi.common.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);



    /**
     * 调用存储过程
     * pluslinout 存储过程名字
     * @param arg
     * @return
     */
    @Procedure("pluslinout")
    Integer explicitlyNamedPluslinout(Integer arg);
    /**
     * 调用存储过程
     * pluslinout 存储过程名字
     * @param arg
     * @return
     */
    @Procedure(procedureName = "pluslinout")
    Integer pluslinout(Integer arg);

    /**
     *  User.pluslIO自定义存储过程的名字
     * @param arg
     * @return
     */
    @Procedure(name = "User.plusl")
    Integer entityAnnotatedCustomNamedProcedurePluslIO(@Param("arg") Integer arg);


}
