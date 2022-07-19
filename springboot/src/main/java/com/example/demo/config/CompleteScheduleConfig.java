package com.example.demo.config;

import cn.hutool.core.date.DateUtil;
import com.example.demo.mapper.EventMapper;
import com.example.demo.mapper.TimeMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class CompleteScheduleConfig implements SchedulingConfigurer {


    @Resource
    TimeMapper timeMapper;

    @Resource
    EventMapper eventMapper;

    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        System.out.println("111");
        taskRegistrar.addTriggerTask(
                //1.add first task content(Runnable)
                () -> {
                    startTask(1);
                    System.out.println("Judge the Morning medication time: " + LocalDateTime.now().toLocalTime());

                },
                //2.Setting the Execution Period(Trigger)
                triggerContext -> {
//                    2.1 Obtaining the execution period from the database
//                    String cron = "00 50 0 * * ?";
                    String cron = "0"+getCornStr(1);

                    //2.2 合法性校验.
                    if (ObjectUtils.isEmpty(cron)) {
                        // Omitted Code
                        System.out.println("can't get the cron ");
                    }
                    //2.3 return the Execution Period(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );

        taskRegistrar.addTriggerTask(
                //1.add second task content(Runnable)
                () -> {
                    startTask(2);
                    System.out.println("Judge the Noon medication time: " + LocalDateTime.now().toLocalTime());

                },
                //2.Setting the Execution Period(Trigger)
                triggerContext -> {
//                    2.1 Obtaining the execution period from the database
//                    String cron = "0/2 * * * * 2";

                    String cron = "0"+getCornStr(2);
                    //2.2 合法性校验.
                    if (ObjectUtils.isEmpty(cron)) {
                        // Omitted Code
                        System.out.println("can't get the cron ");
                    }
                    //2.3 return the Execution Period(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );

        taskRegistrar.addTriggerTask(
                //1.add third task content(Runnable)
                () -> {
                    startTask(3);
                    System.out.println("Set the scheduler signal to 1");
                    System.out.println("Judge the Evening medication time: " + LocalDateTime.now().toLocalTime());
                },
                //2.Setting the Execution Period(Trigger)
                triggerContext -> {
//                    2.1 Obtaining the execution period from the database
//                    String cron = "0/2 * * * * 2";

                    String cron = "0"+getCornStr(3);
                    //2.2 合法性校验.
                    if (ObjectUtils.isEmpty(cron)) {
                        // Omitted Code
                        System.out.println("can't get the cron ");
                    }
                    //2.3 return the Execution Period(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );

        taskRegistrar.addTriggerTask(
                //1.add fourth task content(Runnable)
                () -> {
                    restScheduler(1);
                    System.out.println("Reset the scheduler data: " + LocalDateTime.now().toLocalTime());

                },
                //2.Setting the Execution Period(Trigger)
                triggerContext -> {
                    String cron = "3"+getCornStr(3);
                    //2.2 合法性校验.
                    if (ObjectUtils.isEmpty(cron)) {
                        // Omitted Code
                        System.out.println("can't get the cron ");
                    }
                    //2.3 return the Execution Period(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );

        taskRegistrar.addTriggerTask(
                //1.add fourth task content(Runnable)
                () -> {
                    restScheduler(2);
                    System.out.println("Reset the scheduler data: " + LocalDateTime.now().toLocalTime());

                },
                //2.Setting the Execution Period(Trigger)
                triggerContext -> {
                    String cron =  "3"+getCornStr(2);
                    //2.2 合法性校验.
                    if (ObjectUtils.isEmpty(cron)) {
                        // Omitted Code
                        System.out.println("can't get the cron ");
                    }
                    //2.3 return the Execution Period(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );


        taskRegistrar.addTriggerTask(
                //1.add fourth task content(Runnable)
                () -> {
                    restScheduler(3);
                    System.out.println("ReSet the scheduler signal to 0");
                    System.out.println("Reset the scheduler data: " + LocalDateTime.now().toLocalTime());

                },
                //2.Setting the Execution Period(Trigger)
                triggerContext -> {
                    String cron =  "3"+getCornStr(3);
                    //2.2 合法性校验.
                    if (ObjectUtils.isEmpty(cron)) {
                        // Omitted Code
                        System.out.println("can't get the cron ");
                    }
                    //2.3 return the Execution Period(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

    public String getCornStr(int timeOfDay){

        String []dayOfWeekList={"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
        String []shortWeek={"SUN","MON","TUE","WED","THU","FRI","SAT"};
        //2.1 从数据库获取执行周期
        Calendar c = Calendar.getInstance();
        int indexOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = dayOfWeekList[indexOfWeek-1];

        //get the latest medication time Cron expression
        String timeTest = timeMapper.getTime(dayOfWeek).get(timeOfDay-1);
        Date datetime = DateUtil.parse(timeTest+":00");
//                    System.out.println(datetime);
        c.setTime(datetime);
//                    System.out.println(c);
        c.add(Calendar.MINUTE,30);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);;
        String indexWeek=shortWeek[indexOfWeek-1];
        String cron =" "+minutes+" "+hour+" * * "+indexWeek;

        return cron;
    }

    public List<String> getTimeRange(){
        String []dayOfWeekList={"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
        Calendar c = Calendar.getInstance();
        int indexOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = dayOfWeekList[indexOfWeek-1];
        //get the latest medication time Cron expression
        return timeMapper.getTime(dayOfWeek);

    }

    //reset the Scheduler Signal
    public int restScheduler(Integer id){
        return timeMapper.restScheduler(id);

    }

    // Change the Scheduler Signal to 1
    public int changeScheduler(int indexOfDay){
        return timeMapper.changeScheduler(indexOfDay);
    }

    public void startTask(int taskIndex){
        String []dayOfWeekList={"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
        Calendar c = Calendar.getInstance();
        int indexOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek = dayOfWeekList[indexOfWeek-1];
//      select the event where opened compartment ==dayOfWeek and dayOfWeek ==dayOfWeek
        List<String> openToday =eventMapper.openToday(dayOfWeek);

//      get the responding time slot
        String startTime = getTimeRange().get(taskIndex-1)+":00";
        Date left = DateUtil.parse(startTime);
        Date right = DateUtil.offsetMinute(left, 30);
        int signal = 0;
        for (String a : openToday) {
            System.out.println(a);
            Date singleTime = DateUtil.parse(a);
            if (singleTime.after(left) && singleTime.before(right)){
                signal = 1;
            }
        }
        if (signal==0){
            changeScheduler(taskIndex);
            System.out.println("The compartment has been opened, signal was written to database!");
        }
    }

}