package com.step.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
@Slf4j
public class HelloJob implements BaseJob {
    public static int i=0;
    @Override
    public void execute(JobExecutionContext context){
        log.info("Hello Job执行时间: " + new Date()+"---------------------------"+i++);
    }
}
