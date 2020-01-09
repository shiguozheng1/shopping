package com.step.dao.impl;

import com.step.dao.DbCpUserDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by zhushubin  on 2019-06-27.
 * email:604580436@qq.com
 */
@Repository
public class DbCpUserDaoImpl implements DbCpUserDao{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Long findByUserId(String userId) {
        String sql ="";
        entityManager.createNativeQuery(sql);
        return null;
    }
}
