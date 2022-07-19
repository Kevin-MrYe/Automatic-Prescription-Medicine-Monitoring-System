package com.example.demo.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Time;
import com.example.demo.mapper.TimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/time")

public class TimeController {

    @Resource
    TimeMapper TimeMapper;

    @Autowired
    private JavaMailSender javaMailSender;


    @PutMapping
    public Result<?> update(@RequestBody Time Time){
        TimeMapper.updateById(Time);
        return Result.success();
    }

    @PostMapping
    public Result<?> save(@RequestBody Time Time){
        TimeMapper.insert(Time);
        return Result.success();
    }

    @GetMapping("/{dayOfWeek}")
    public List<?> save(@PathVariable("dayOfWeek") String strDateTime){
        QueryWrapper<Time> temper = new QueryWrapper<>();
        temper.select(strDateTime);
        List<Object> userList = TimeMapper.selectObjs(temper);   //choose column data
        return userList;
    }

    @GetMapping("/checkScheduler")
    public String checkScheduler(){
//        Date date = DateUtil.date();
//        String datetime = DateUtil.format(date,"yyyy-MM-dd   HH:mm:ss");
//        System.out.println(datetime);
//
//        String []dayOfWeekList={"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
//        int indexOfWeek = DateUtil.dayOfWeek(date);
//        String dayOfWeek = dayOfWeekList[indexOfWeek-1];
//        String morning = TimeMapper.getTime(dayOfWeek).get(0)+":00";
//        String noon = TimeMapper.getTime(dayOfWeek).get(1)+":00";
//        String evening = TimeMapper.getTime(dayOfWeek).get(2)+":00";
//
//        Date rightMorning = DateUtil.offsetMinute(DateUtil.parse(morning),30);
//        Date rightNoon = DateUtil.offsetMinute(DateUtil.parse(noon),30);
//        Date rightEvening = DateUtil.offsetMinute(DateUtil.parse(evening),30);
//        Date currentTime = DateUtil.parse(datetime);
//        boolean alarmSignal = false;
//        List<Integer> schedulerList =TimeMapper.checkScheduler();
//        for(Integer signal: schedulerList){
//            if(signal == 1) {
//                alarmSignal = true;
//                break;
//            }
//        }
        Date time1 = DateUtil.date();
        System.out.println(time1);
//        if(alarmSignal) {
//            //send email
//            SimpleMailMessage msg = new SimpleMailMessage();
//            msg.setTo("mrbinye@gmail.com");
//            msg.setSubject("Testing from Spring Boot");
//            msg.setText("Hello World \n Spring Boot Email\n Delete successfully");
//            javaMailSender.send(msg);
//            return "#1";//send an alarm
//        }
//        else {
//            SimpleMailMessage msg = new SimpleMailMessage();
//            msg.setTo("mrbinye@gmail.com");
//            msg.setSubject("Testing from Spring Boot");
//            msg.setText("Hello World \n Spring Boot Email\n Delete successfully");
//            javaMailSender.send(msg);
//            return "#0";// not send an alarm
//        }
        return "#0";
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<Time> wrapper = Wrappers.<Time>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Time::getId, search);
        }
        Page<Time> TimePage = TimeMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(TimePage);
    }



}
