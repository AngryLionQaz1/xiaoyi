package com.snow.xiaoyi.module.test;

import com.snow.xiaoyi.common.bean.Result;
import com.snow.xiaoyi.common.pojo.Authority;
import com.snow.xiaoyi.common.pojo.Role;
import com.snow.xiaoyi.common.pojo.User;
import com.snow.xiaoyi.common.repository.AuthorityRepository;
import com.snow.xiaoyi.common.repository.RoleRepository;
import com.snow.xiaoyi.common.repository.UserRepository;
import com.snow.xiaoyi.config.annotation.Decrypt;
import com.snow.xiaoyi.config.annotation.Encrypt;
import com.snow.xiaoyi.config.annotation.SecurityPermission;
import com.snow.xiaoyi.config.token.JWTToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Api(tags = "测试")
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestService testService;
    @Autowired
    private JWTToken jwtToken;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;








    @GetMapping("sexs")
    @Encrypt
    public Result sexs(){
        return Result.success("好");
    }

    @PostMapping("usersx")
    @Decrypt
    public Result usersx(@RequestBody Result result){
        return result;
    }


    @PostMapping("usersx2")
    @Decrypt
    @Encrypt
    public Result usersx2(@RequestBody Result result){
        return result;
    }

    @GetMapping("users")
    public ResponseEntity users(){

        List<User> users=userRepository.findAll();
        users.forEach(k->{
            k.setToken(jwtToken.createToken(    String.valueOf(k.getId())));
        });

        return ResponseEntity.ok(users);
    }

    @PostMapping("userReduceRole")
    public ResponseEntity userReduceRole(@RequestParam Long userId,
                                         @RequestParam Long roleId){
        Optional<Role> o=roleRepository.findById(roleId);
        if (!o.isPresent())return ResponseEntity.notFound().build();
        Optional<User> r=userRepository.findById(userId);
        if (!r.isPresent())return ResponseEntity.notFound().build();
        User user=r.get();
        List<Role> roles=user.getRoles();
        roles.remove(o.get());
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("userAddRole")
    public ResponseEntity userAddRole(@RequestParam Long userId,
                                      @RequestParam Long roleId){
        Optional<Role> o=roleRepository.findById(roleId);
        if (!o.isPresent())return ResponseEntity.notFound().build();
        Optional<User> r=userRepository.findById(userId);
        if (!r.isPresent())return ResponseEntity.notFound().build();

        User user=r.get();
        List<Role> roles=user.getRoles();
        roles.add(o.get());
        user.setRoles(roles);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("addUser")
    public ResponseEntity addUser(@RequestParam Long roleId,
                                  @RequestParam String name){
        Optional<Role> o=roleRepository.findById(roleId);
        if (!o.isPresent())return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userRepository.save(User.builder().username(name).build()));
    }


    @GetMapping("roles")
    public ResponseEntity roles(){
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @PostMapping("addRole")
    public ResponseEntity addRole(@RequestParam Long authorityId,
                                  @RequestParam String name){
        Optional<Authority> o=authorityRepository.findById(authorityId);
        if (!o.isPresent())return ResponseEntity.notFound().build();
        Role role=roleRepository.save( Role.builder().name(name).authorities(Arrays.asList(o.get())).build());
        return ResponseEntity.ok(role);
    }


    @GetMapping("authorities")
    public ResponseEntity authorities(){
        return ResponseEntity.ok(authorityRepository.findAll());
    }

    @PostMapping("addAuthority")
    @ApiOperation(value = "添加权限")
    public ResponseEntity addAuthority(@RequestParam String name,
                                       @RequestParam String uri,
                                       @RequestParam String details){
        return ResponseEntity.ok(authorityRepository.save(Authority.builder().name(name).uri(uri).details(details).build()));
    }


    @GetMapping
    @ApiOperation(value = "测试")
    @SecurityPermission
    public ResponseEntity test(){
        return ResponseEntity.ok(jwtToken.createToken("222222222"));
    }

    @GetMapping("path")
    @SecurityPermission
    @ApiOperation(value = "获取地址")
    public ResponseEntity path(){
        return ResponseEntity.ok(testService.path());
    }

    @PostMapping("upload")
    @SecurityPermission
    @ApiOperation(value = "上传文件")
    public ResponseEntity uploadFle(@RequestParam MultipartFile file){
        return ResponseEntity.ok(testService.uploadFile(file));
    }




}



