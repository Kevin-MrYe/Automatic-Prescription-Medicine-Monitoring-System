package com.example.demo.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@TableName("event")
@Data

public class Event {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String datetime;
    private String dayOfweek;
    private String pickStatus;
    private String openStatus;
    private String globalStatus;
    private String compartment;
    private String event;


}
