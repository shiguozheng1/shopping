package com.step.dao.impl;

import com.step.dao.TemplateDao;
import com.step.dao.UserDao;
import com.step.entity.secondary.Admin;
import com.step.entity.secondary.workflow.SimpleTemplateEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * shigz
 * 2019/11/14
 **/
@Repository
@Transactional("transactionManagerSecondary")
public class TemplateDaoImpl extends BaseDaoImpl<SimpleTemplateEntity, Long>  implements TemplateDao {
    @PersistenceContext(unitName="entityManagerFactorySecondary")
    protected EntityManager entityManager;
    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @Transactional(value = "transactionManagerSecondary",readOnly = true)
    public boolean exists(String attributeName, Object attributeValue) {
        return super.exists(attributeName, attributeValue);
    }
}
