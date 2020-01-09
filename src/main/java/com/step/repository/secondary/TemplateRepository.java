package com.step.repository.secondary;
import com.step.entity.secondary.workflow.SimpleTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * shigz
 * 2019/11/20
 * 模板管理
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface TemplateRepository extends JpaRepository<SimpleTemplateEntity,Long> , JpaSpecificationExecutor<SimpleTemplateEntity> {
}
