package com.step.action;

import com.step.entity.secondary.form.JobInfoForm;
import com.step.entity.secondary.vo.JobDetailVo;
import com.step.service.JobAndTriggerService;
import com.step.utils.StringCommonUtils;
import com.step.utils.tools.DateUnit;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.quartz.DateBuilder.futureDate;

/**
 * 定时任务
 *
 */
@RestController
@RequestMapping("job")
@Slf4j
public class ScheduleJobController extends BaseController{
    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;
    @Autowired
    private DateUnit dateUnit;

    @Autowired
    private JobAndTriggerService jobAndTriggerService;
    @GetMapping(value = "/check")
    public Wrapper verifyClass( String jobClassName){
        Class<?>  aClass=null;
        try {
            aClass= Class.forName(jobClassName);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        if(aClass!=null) {
            return WrapMapper.ok();
        }
        return WrapMapper.error("类名不存在");
    }


    /**
     * 新增定时任务
     *
     * @param jobInfo
     * @return
     */
    @PostMapping
    public Wrapper add(@RequestBody JobInfoForm jobInfo){
       List<String> ret= validater(jobInfo);
        if(ret!=null && ret.size()>0){
          return  WrapMapper.error(ret.toString());
        }
        try {
            if (jobInfo.getTimeType() == null) {
                addCronJob(jobInfo);
                return WrapMapper.ok();
            }
            addSimpleJob(jobInfo);
        } catch (Exception e) {
           e.printStackTrace();
            return WrapMapper.error(e.getMessage());
        }
     return null;
    }


    /**
     * 暂停任务
     * @param jobInfoForm
     * @throws Exception
     */
    @PostMapping(value = "/pause")
    public Wrapper pause(@RequestBody  JobInfoForm jobInfoForm) {
        try{
            scheduler.pauseJob(jobInfoForm.getKey());
        }catch (SchedulerException e){
            return WrapMapper.error(e.getMessage());
        }
        return WrapMapper.ok();
    }

    /**
     * 恢复任务
     *
     * @param jobInfoForm
     * @throws Exception
     */
    @PostMapping(value = "/resume")
    public Wrapper<Object> resume(@RequestBody  JobInfoForm jobInfoForm){
        try{
            scheduler.resumeJob(jobInfoForm.getKey());
        }catch (SchedulerException e){
            return WrapMapper.error(e.getMessage());
        }
        return WrapMapper.ok();

    }

    /**
     * 删除任务
     * 删除操作前应该暂停该任务的触发器，并且停止该任务的执行
     *
     * @param jobInfoForm
     * @throws Exception
     */
    @DeleteMapping
    public Wrapper delete(@RequestBody  JobInfoForm jobInfoForm) {
   //     scheduler.pauseTrigger(jobInfoForm.getTriggerKey());
//        scheduler.unscheduleJob(jobInfoForm.getTriggerKey());
        try{
            boolean deleteJob = scheduler.deleteJob(jobInfoForm.getKey());
            if(deleteJob){
                return  WrapMapper.ok();
            }
            return WrapMapper.error("删除失败");
        }catch (SchedulerException e){
            return WrapMapper.error(e.getMessage());
        }

    }

    /**
     * 更新任务
     *
     * @param jobInfoForm
     * @throws Exception
     */
    @PostMapping(value = "/reschedule")
    public void reschedule(@RequestBody  JobInfoForm jobInfoForm) throws Exception {
        try {
            TriggerKey triggerKey = jobInfoForm.getTriggerKey();
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfoForm.getCronExpression());

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            System.out.println("更新定时任务失败" + e);
            throw new Exception("更新定时任务失败");
        }
    }



    /**
     * 查询任务
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/page")
        public Wrapper queryByPage(@RequestParam(value = "pageNum") Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize) {
            Pageable pageable = PageRequest.of(pageNum,pageSize);
            Page<JobDetailVo> page = jobAndTriggerService.getJobAndTriggerDetailsByPage(pageable);
            return WrapMapper.ok(page);
}

    private void addSimpleJob(JobInfoForm jobInfo) throws Exception {

        // 启动调度器
        scheduler.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(StringCommonUtils.getClass(jobInfo.getJobClassName()).getClass())
                .withIdentity(jobInfo.getJobClassName(), jobInfo.getJobGroupName())
                .build();
        DateBuilder.IntervalUnit verDate = dateUnit.verification(jobInfo.getTimeType());
        SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity(jobInfo.getJobClassName(), jobInfo.getJobGroupName())
                .startAt(futureDate(Integer.parseInt(jobInfo.getCronExpression()), verDate))
                .forJob(jobInfo.getJobClassName(), jobInfo.getJobGroupName())
                .build();

        try {
            scheduler.scheduleJob(jobDetail, simpleTrigger);

        }catch (ObjectAlreadyExistsException e){
            throw new ObjectAlreadyExistsException("定时任务已存在");
        } catch (SchedulerException e) {
            throw new Exception("创建定时任务失败");
        }
    }

    //CronTrigger
    public void addCronJob(JobInfoForm jobInfoForm) throws Exception {
        // 启动调度器
        scheduler.start();
        try {
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(StringCommonUtils.getClass(jobInfoForm.getJobClassName()).getClass()).
                withIdentity(jobInfoForm.getJobClassName(), jobInfoForm.getJobGroupName())
                .build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfoForm.getCronExpression());
        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().
                withIdentity(jobInfoForm.getJobClassName(), jobInfoForm.getJobGroupName())
                .withSchedule(scheduleBuilder)
                .build();

            scheduler.scheduleJob(jobDetail, trigger);
        }catch (ObjectAlreadyExistsException e){
          throw  new ObjectAlreadyExistsException("定时任务已存在");
        }catch (RuntimeException e){
           throw new RuntimeException("Cron格式不正确");
        } catch (SchedulerException e) {
            throw new Exception("创建定时任务失败");
        }
    }
}
