package com.example.demo.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/user")

public class UserController {

    @Resource
    UserMapper userMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) throws ParseException {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword()));
        if (res == null){
            return Result.error("-1","Username or Password  is incorrect");
        }


        return Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if (res != null){
            return Result.error("-1","Username already exists!");
        }
        if(user.getPassword() == null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        System.out.println("out: "+user.getEmail());
        return Result.success();

    }

    @PostMapping
    public Result<?> save(@RequestBody User user){
        userMapper.insert(user);
        System.out.println(user);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody User user){
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        userMapper.deleteById(id);

        //send email
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("allenwin857@outlook.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email\n Delete successfully");
        javaMailSender.send(msg);
        return Result.success();
    }

}
