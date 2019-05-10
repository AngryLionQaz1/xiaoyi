/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: UserService
 * Author:   萧毅
 * Date:     2019/2/21 10:25
 * Description:
 */
package com.snow.xiaoyi.module.user;


import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.common.mapper.BaseMapper;
import com.snow.xiaoyi.common.pojo.Permissions;
import com.snow.xiaoyi.common.pojo.Role;
import com.snow.xiaoyi.common.pojo.User;
import com.snow.xiaoyi.common.repository.UserRepository;
import com.snow.xiaoyi.config.security.SecurityContextHolder;
import com.snow.xiaoyi.module.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityContextHolder securityContextHolder;
    @Resource
    private BaseMapper baseMapper;
    @Autowired
    private TestService testService;


    public Result auths() {

        Long userId=securityContextHolder.getUser();
        List<Permissions> permissions = baseMapper.userPermissions(String.valueOf(userId));
        return Result.success(testService.auth(permissions));

    }


    @Cacheable(key="'user_'+#id",value="'user'+#id")
    public User users(Long id) {
        Optional<User> byUsername = userRepository.findById(id);
        if (!byUsername.isPresent())return null;
        System.out.println(id + "进入实现类获取数据！");
        User user=byUsername.get();
        List<Role> roles=user.getRoles();
        System.out.println(roles.get(0).getPermissions().get(0).getUri());
        return user;
    }



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