/**
 * Copyright (C), 2015-2019, 重庆了赢科技有限公司
 * FileName: UserController
 * Author:   萧毅
 * Date:     2019/2/20 15:18
 * Description:
 */
package com.snow.xiaoyi.module.user;

import com.snow.xiaoyi.common.mapper.UserMapper;
import com.snow.xiaoyi.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;



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




}