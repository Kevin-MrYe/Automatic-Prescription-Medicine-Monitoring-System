package com.example.demo.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Event;
import com.example.demo.entity.Time;
import com.example.demo.mapper.EventMapper;
import com.example.demo.mapper.TimeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/event")

public class EventController {

    @Value("${server.port}")
    private String port;

    @Value("${file.ip}")
    private String ip;

    @Resource
    EventMapper EventMapper;
    TimeMapper TimeMapper;

    @Autowired
    private JavaMailSender javaMailSender;

//    @PostMapping(value="/{temp}")
//    public String addUser(@PathVariable String temp) {

//    }

    @PostMapping
    public Result<?> save(@RequestBody Event Event){
        String[] dayweek ={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        Date date = DateUtil.parse(Event.getDatetime());
        DateTime newDate = DateUtil.offsetHour(date, 1);
        String format = DateUtil.format(newDate, "yyyy-MM-dd   HH:mm:ss");
        int dayOfweek = DateUtil.dayOfWeek(newDate);
        Event.setDatetime(format);
        Event.setDayOfweek(dayweek[dayOfweek-1]);
        EventMapper.insert(Event);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Event Event){
        String[] dayweek ={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        Date date = DateUtil.parse(Event.getDatetime());
        DateTime newDate = DateUtil.offsetHour(date, 1);
        String format = DateUtil.format(newDate, "yyyy-MM-dd   HH:mm:ss");
        int dayOfweek = DateUtil.dayOfWeek(newDate);
        Event.setDatetime(format);
        Event.setDayOfweek(dayweek[dayOfweek-1]);
        EventMapper.updateById(Event);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        EventMapper.deleteById(id);

        //send email
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo("allenwin857@outlook.com");
//        msg.setSubject("Testing from Spring Boot");
//        msg.setText("Hello World \n Spring Boot Email\n Delete successfully");
//        javaMailSender.send(msg);
        return Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) throws ParseException {
        LambdaQueryWrapper<Event> wrapper = Wrappers.<Event>lambdaQuery();
        wrapper.orderByDesc(Event::getDatetime);
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Event::getEvent, search);
        }
        Page<Event> EventPage = EventMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        
//        String dateStr = "21:40"+":00";
//        Date leftTime = DateUtil.parse(dateStr);
//        System.out.println(leftTime);
//        Date rightTime = DateUtil.offsetMinute(leftTime, 30);
//        System.out.println(rightTime);
//        Date now1 = new Date();
//        System.out.println(DateUtil.format(now1,"yyyy-MM-dd HH:mm:ss"));
//
////        Date now1 = DateUtil.parse("08:20"+":00");
////        System.out.println(now1);
//        if (now1.after(leftTime) && now1.before(rightTime)) {
//            System.out.println("in the right medication time");
//        }
//        else {
//            System.out.println("in the wrong medication time");
//        }
        return Result.success(EventPage);
    }

    @GetMapping("/test")
    public Result<?> save(){
        QueryWrapper<Time> temper = new QueryWrapper<>();
        temper.select("sunday");
        List<Map<String,Object>> userList = TimeMapper.selectMaps(temper);   //choose column data

//        List<Arduino> res = ArduinoMapper.selectList(null);//choose all data
        return Result.success(userList);
    }

    @GetMapping(value = "/{dateTime}/{switchSignal}/{accSignal}")
    public String getDateFromArduino(@PathVariable("dateTime") String strDateTime,
                          @PathVariable("switchSignal")  String switchSignal,
                          @PathVariable("accSignal")  String accSignal) {
//        System.out.println(dateTime);
        Date dateTime = DateUtil.parse(strDateTime);
        Event event = new Event();
        String strTime = DateUtil.format(dateTime, "yyyy-MM-dd   HH:mm:ss");
        System.out.println(ip);
        System.out.println(port);
//        System.out.println(ip);
//        System.out.println(accSignal);
//        System.out.println(switchSignal);

        //Set the PickStatus, OpenStatus , OpenCompartment
        String OpenStatus = (switchSignal.equals("0"))?"No":"Yes";
        String PickStatus = (accSignal.equals("0"))?"No":"Yes";
        String GlobalStatus = null;
        String eventLog = null;
        String returnSignal = null;
        String[] dayWeek ={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        int indexOfWeek = DateUtil.dayOfWeek(dateTime);
        int numCompartment = Integer.parseInt(switchSignal);
        String dayOfWeek = dayWeek[indexOfWeek-1];
        String OpenCompartment = (Objects.equals(switchSignal, "0"))?"Null":dayWeek[numCompartment-1];

        //get the normal medication time from database
        String uri = "http://"+ip+":"+port+"/time/"+dayOfWeek;
//        System.out.print("Today is:");
//        System.out.println(dayOfWeek);
//        System.out.print("Open compartment is:");
//        System.out.println(OpenCompartment);

//        //get the timestamp from time Api
        RestTemplate restTemplate = new RestTemplate();
        List<?> result = restTemplate.getForObject(uri,List.class);
        String morning = String.valueOf(Objects.requireNonNull(result).get(0))+":00";
        String noon = String.valueOf(result.get(1))+":00";
        String evening = String.valueOf(result.get(2))+":00";
//        System.out.println(morning);

        //get the right medication time range
        Date leftMorning = DateUtil.parse(morning);
        Date rightMorning = DateUtil.offsetMinute(leftMorning, 30);
        Date leftNoon = DateUtil.parse(noon);
        Date rightNoon = DateUtil.offsetMinute(leftNoon, 30);
        Date leftEvening = DateUtil.parse(evening);
        Date rightEvening = DateUtil.offsetMinute(leftEvening, 30);

        //judge if the status is normal and set the eventLog
        if( PickStatus.equals("Yes") && OpenStatus.equals("Yes")){
            if (dayOfWeek.equals(OpenCompartment)){
                if((dateTime.after(leftMorning) && dateTime.before(rightMorning)) ||
                        (dateTime.after(leftNoon) && dateTime.before(rightNoon)) ||
                        (dateTime.after(leftEvening) && dateTime.before(rightEvening))){
                    GlobalStatus = "Normal";
                    eventLog = "Picked up the pillbox and opened the compartment in the right medication time";
                }else{
                    GlobalStatus = "Abnormal";
                    eventLog = "Picked up the pillbox and opened the compartment in the wrong medication time";
                }
            }else {
                GlobalStatus = "Abnormal";
                eventLog = "Opened incorrect compartment";
            }
        }

        if( PickStatus.equals("Yes") && OpenStatus.equals("No")){
            if((dateTime.after(leftMorning) && dateTime.before(rightMorning)) ||
                    (dateTime.after(leftNoon) && dateTime.before(rightNoon)) ||
                    (dateTime.after(leftEvening) && dateTime.before(rightEvening))){
                GlobalStatus = "Normal";
                eventLog = "Picked up the pillbox in the right medication time";
            }else{
                GlobalStatus = "Abnormal";
                eventLog = "Picked up the pillbox in the wrong medication time";
            }
        }

        if( PickStatus.equals("No") && OpenStatus.equals("Yes")){
            if (dayOfWeek.equals(OpenCompartment)){
                if((dateTime.after(leftMorning) && dateTime.before(rightMorning)) ||
                        (dateTime.after(leftNoon) && dateTime.before(rightNoon)) ||
                        (dateTime.after(leftEvening) && dateTime.before(rightEvening))){
                    GlobalStatus = "Normal";
                    eventLog = "Opened the compartment in the right medication time";
                }else{
                    GlobalStatus = "Abnormal";
                    eventLog = "Opened the compartment in the wrong medication time";
                }
            }else {
                GlobalStatus = "Abnormal";
                eventLog = "Opened incorrect compartment";
            }
        }

        returnSignal = (GlobalStatus.equals("Normal"))?"#0":"#1";





        //insert the data from arduino
        event.setDatetime(strTime);
        event.setDayOfweek(dayWeek[indexOfWeek-1]);
        event.setPickStatus(PickStatus);
        event.setOpenStatus(OpenStatus);
        event.setCompartment(OpenCompartment);
        event.setGlobalStatus(GlobalStatus);
        event.setEvent(eventLog);
        if (PickStatus.equals("Yes") || OpenStatus.equals("Yes")){
            System.out.println("The operation time is:"+event.getDatetime());
            System.out.println("The day of this week is:"+event.getDayOfweek());
            System.out.println("The pick status is:"+event.getPickStatus());
            System.out.println("The open status is:"+event.getOpenStatus());
            System.out.println("The opened compartment is:"+event.getCompartment());
            System.out.println("The global status is:"+event.getGlobalStatus());
            System.out.println("The event log is:"+event.getEvent());
            System.out.println();
        }
        if(returnSignal.equals("#1")){
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo("mrbinye@gmail.com");
            msg.setSubject("Alarm email from APMM smart pill box");
            msg.setText("Hello, \n\n At "+strTime+", the patient "+eventLog+". Please check the detail on web application right now!");
            javaMailSender.send(msg);
        }

        EventMapper.insert(event);
        return returnSignal;
    }
}
