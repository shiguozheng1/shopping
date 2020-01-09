package com.step.service.impl;

import com.step.entity.secondary.vo.JobDetailVo;
import com.step.service.JobAndTriggerService;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/***
 * 定时任务
 */
@Service
@Transactional("transactionManagerSecondary")
public class JobAndTriggerServiceImpl implements JobAndTriggerService {
    @Autowired
    @Qualifier("entityManagerFactorySecondary")
    private EntityManager entityManager;


    @Override
    public Page<JobDetailVo> getJobAndTriggerDetailsByPage(Pageable pageable) {

        String  sql = "SELECT DISTINCT QRTZ_JOB_DETAILS.JOB_NAME as jobName ,\n" +
                "\tQRTZ_JOB_DETAILS.JOB_GROUP as jobGroup ,\n" +
                "\tQRTZ_JOB_DETAILS.JOB_CLASS_NAME  as jobClassName,\n" +
                "\tQRTZ_TRIGGERS.TRIGGER_NAME  as triggerName,\n" +
                "\tQRTZ_TRIGGERS.TRIGGER_GROUP as triggerGroup,\n" +
                "\tQRTZ_CRON_TRIGGERS.CRON_EXPRESSION as cronExpression,\n" +
                "\tQRTZ_CRON_TRIGGERS.TIME_ZONE_ID as timeZoneId,\n" +
                "\tQRTZ_TRIGGERS.TRIGGER_STATE as triggerState\n" +
                "FROM\n" +
                "\tQRTZ_JOB_DETAILS\n" +
                "INNER JOIN QRTZ_TRIGGERS ON QRTZ_TRIGGERS.TRIGGER_GROUP = QRTZ_JOB_DETAILS.JOB_GROUP\n" +
                "INNER JOIN QRTZ_CRON_TRIGGERS ON QRTZ_JOB_DETAILS.JOB_NAME = QRTZ_TRIGGERS.JOB_NAME\n" +
                "AND QRTZ_TRIGGERS.TRIGGER_NAME = QRTZ_CRON_TRIGGERS.TRIGGER_NAME\n" +
                "AND QRTZ_TRIGGERS.TRIGGER_GROUP = QRTZ_CRON_TRIGGERS.TRIGGER_GROUP";
        List<JobDetailVo> result= entityManager.createNativeQuery(sql)
                .unwrap(NativeQuery.class)
                .setResultTransformer(Transformers.aliasToBean(JobDetailVo.class)).getResultList();

        String  countSql = "SELECT count(*) FROM" +
                "\tQRTZ_JOB_DETAILS\n" +
                "LEFT JOIN QRTZ_TRIGGERS ON QRTZ_TRIGGERS.TRIGGER_GROUP = QRTZ_JOB_DETAILS.JOB_GROUP\n" +
                "LEFT JOIN QRTZ_CRON_TRIGGERS ON QRTZ_JOB_DETAILS.JOB_NAME = QRTZ_TRIGGERS.JOB_NAME\n" +
                "AND QRTZ_TRIGGERS.TRIGGER_NAME = QRTZ_CRON_TRIGGERS.TRIGGER_NAME\n" +
                "AND QRTZ_TRIGGERS.TRIGGER_GROUP = QRTZ_CRON_TRIGGERS.TRIGGER_GROUP";

        Integer total = (Integer) entityManager.createNativeQuery(countSql).getSingleResult();

      Page<JobDetailVo> page = new PageImpl<JobDetailVo>(result,pageable,total);

      return page;
    }
}
