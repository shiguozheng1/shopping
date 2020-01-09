package com.step.entity.secondary.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class JobDetailVo implements Serializable {
    private String jobName;
    private String jobGroup;
    private String jobClassName;
    private String  triggerName;
    private String  triggerGroup;
    private String  cronExpression;
    private String  timeZoneId;
    private String  triggerState;
}
