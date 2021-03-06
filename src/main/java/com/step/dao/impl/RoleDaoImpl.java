package com.step.dao.impl;

import com.step.dao.MenuDao;
import com.step.dao.RoleDao;
import com.step.entity.secondary.Menu;
import com.step.entity.secondary.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by zhushubin  on 2019-11-04.
 * email:604580436@qq.com
 */
@Repository
@Transactional("transactionManagerSecondary")
public class RoleDaoImpl extends BaseDaoImpl<Role, Long> implements RoleDao {
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
