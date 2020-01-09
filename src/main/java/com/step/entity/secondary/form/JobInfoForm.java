package com.step.entity.secondary.form;

import lombok.Data;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import javax.validation.constraints.NotEmpty;
import java.beans.Transient;

/**
 * Created by haoxy on 2018/10/8.
 */
@Data
public class JobInfoForm {
    @NotEmpty(message = "jobClassName cannot be empty")
    private String jobClassName;
    @NotEmpty(message = "jobGroupName cannot be empty")
    private String jobGroupName;
    @NotEmpty(message = "cronExpression cannot be empty")
    private String cronExpression;

    private String jobType;

    private Integer timeType;

    private String triggerState;

   @Transient
   public JobKey getKey(){
      return  JobKey.jobKey(jobClassName, jobGroupName);
   }
    @Transient
   public TriggerKey getTriggerKey(){
      return TriggerKey.triggerKey(jobClassName, jobGroupName);
   }
}
