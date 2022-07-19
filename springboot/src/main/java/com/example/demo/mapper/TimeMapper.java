package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Time;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface TimeMapper extends BaseMapper<Time> {

    @Select(value = "select ${dayOfWeek} from time")
    public List<String> getTime(@Param("dayOfWeek") String dayOfWeek);


    @Update(value = "update time set scheduler=1 where id=#{id}")
    public int changeScheduler(@Param("id") Integer id);

    @Update(value = "update time set scheduler=0 where id=#{id} ")
    public  int restScheduler(@Param("id") Integer id);

    @Select(value = "select scheduler from time")
    public List<Integer> checkScheduler();


}
