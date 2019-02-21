/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: UserService
 * Author:   萧毅
 * Date:     2019/2/21 10:25
 * Description:
 */
package com.snow.xiaoyi.module.user;


import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.common.pojo.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Cacheable(key="'user_'+#id",value="'user'+#id")
    public Result getUser(String id) {
        System.out.println(id+"进入实现类获取数据！");
        User user = new User();
        user.setId(Long.valueOf(id));
        user.setUsername("香菇,难受");
        user.setPassword("xiaoyi");
        return Result.success(user);
    }

    @CacheEvict(key="'user_'+#id", value="users", condition="#id!=1")
    public void deleteUser(String id) {
        System.out.println(id+"进入实现类删除数据！");
    }


}