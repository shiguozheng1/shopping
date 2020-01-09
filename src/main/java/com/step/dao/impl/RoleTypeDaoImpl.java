package com.step.dao.impl;

import com.step.dao.RoleTypeDao;
import com.step.entity.secondary.RoleType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * shigz
 * 2019/11/5
 **/
@Repository
@Transactional("transactionManagerSecondary")
public class RoleTypeDaoImpl extends BaseDaoImpl<RoleType, Long> implements RoleTypeDao {
    @PersistenceContext(unitName="entityManagerFactorySecondary")
    protected EntityManager entityManager;
    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    @Transactional(value = "transactionManagerSecondary",readOnly = true)
    public boolean exists(String attributeName, Object attributeValue) {
        return super.exists(attributeName, attributeValue);
    }
}