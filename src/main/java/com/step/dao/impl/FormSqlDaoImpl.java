package com.step.dao.impl;

import com.step.dao.FormSqlDao;
import com.step.entity.bean.table.DatabaseMapp;
import com.step.entity.bean.table.TableMapp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhushubin  on 2019-11-18.
 * email:604580436@qq.com
 */
@Repository
public class FormSqlDaoImpl implements FormSqlDao {
    @Override
    public void createTable(DatabaseMapp databaseMapp) {
       List<TableMapp> ret =databaseMapp.getTables();
       return ;
    }

    @Override
    public void deleteTable(String var1, String var2, String var3) {

    }
}
