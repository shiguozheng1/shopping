package com.step.service;

import com.step.entity.secondary.vo.JobDetailVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/***
 * 定时任务
 */
public interface JobAndTriggerService {
    Page<JobDetailVo> getJobAndTriggerDetailsByPage(Pageable pageable);
}
