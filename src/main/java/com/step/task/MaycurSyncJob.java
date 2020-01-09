package com.step.task;

import com.google.common.collect.Lists;
import com.step.utils.JsonUtils;
import com.step.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import com.step.properties.MaycurProperties;
import com.step.service.*;
import com.step.strategy.org.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * shigz
 * 2019/9/11
 *
 * @author user*/
@Slf4j
public class MaycurSyncJob implements BaseJob {
    private static int i=0;
    @Autowired
    private StrategyContext context;
    @Override
    public void execute(JobExecutionContext context1){
        List ret = context.invoke();
        if(ret!=null&&ret.size()>0){
            log.error(JsonUtils.toString(ret));
        }
    }
}
