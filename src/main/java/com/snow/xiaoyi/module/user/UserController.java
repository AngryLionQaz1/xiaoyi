/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: UserController
 * Author:   萧毅
 * Date:     2019/2/20 15:18
 * Description:
 */
package com.snow.xiaoyi.module.user;

import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.common.mapper.UserMapper;
import com.snow.xiaoyi.common.pojo.User;
import com.snow.xiaoyi.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;



    @GetMapping("users")
    public Result users(){
        return Result.success(userRepository.findAll());
    }


    @PostMapping("redis1")
    public void redis1(){

        User user=User.builder().username("小易").password("xiaoyi").id(1L).build();

        template.opsForValue().append("date", user.toString());
        System.out.println(template.opsForValue().get("date"));

    }




    @GetMapping("redis2")
    public void redis2(){
        for (int i = 0; i < 2; i++) {
            User user = (User) userService.getUser(String.valueOf(i)).getData();
            System.out.println(user);
        }
    }

    @PostMapping("redis3")
    public void redis3(){

        User user=User.builder().username("小易").password("xiaoyi").id(1L).build();

        redisTemplate.opsForValue().set("xx",user);
        System.out.println(template.opsForValue().get("xx"));

    }







    @GetMapping("test1")
    public ResponseEntity test1(){


        return ResponseEntity.ok(userRepository.pluslinout(10));
    }




    @GetMapping("test2")
    public ResponseEntity test2(){


        return ResponseEntity.ok(userRepository.explicitlyNamedPluslinout(10));
    }


    @GetMapping("test3")
    public ResponseEntity test3(){


        return ResponseEntity.ok(userRepository.entityAnnotatedCustomNamedProcedurePluslIO(10));
    }

    @GetMapping("test4")
    public ResponseEntity test4(){

        Map<String,Integer> map=new HashMap<>();
        map.put("arg",100);
        map.put("res",0);
        userMapper.testx(map);

        return ResponseEntity.ok(map.get("res"));
    }


    @GetMapping("test5")
    public ResponseEntity test5(){

        Map<String,Integer> map=new HashMap<>();
        map.put("iid",1);

        User user=userMapper.findById(map);

        return ResponseEntity.ok(user);
    }


}