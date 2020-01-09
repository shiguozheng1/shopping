package com.step.repository.secondary;
import com.step.entity.secondary.workflow.DynamicFormColumnEntity;
import com.step.entity.secondary.workflow.DynamicFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * shigz
 * 2019/11/20
 * 表单设计
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface DynamicFormColumnEntityRepository extends JpaRepository<DynamicFormColumnEntity,Long> , JpaSpecificationExecutor<DynamicFormColumnEntity> {

}
