package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Event;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EventMapper extends BaseMapper<Event> {

    @Select(value = "select datetime from event where day_ofweek=#{dayOfWeek} and compartment=#{dayOfWeek}")
    List<String> openToday(@Param("dayOfWeek") String dayOfWeek);
}
