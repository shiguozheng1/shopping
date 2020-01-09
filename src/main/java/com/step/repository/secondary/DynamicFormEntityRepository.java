package com.step.repository.secondary;

/**
 * shigz
 * 2019/11/25
 **/

import com.step.entity.secondary.workflow.DynamicFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
/* 表单设计
        **/
@Repository
@Transactional("transactionManagerSecondary")
public interface DynamicFormEntityRepository extends JpaRepository<DynamicFormEntity,Long>, JpaSpecificationExecutor<DynamicFormEntity> {

}
