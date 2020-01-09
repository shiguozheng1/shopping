package com.step.dao;

import com.step.entity.bean.table.DatabaseMapp;

import java.util.List;

/**
 * Created by zhushubin  on 2019-11-18.
 * email:604580436@qq.com
 * 根据表单创建数据库表
 */
public interface FormSqlDao {
    /***
     * 创建数据库表
     * @param databaseMapp
     */
    void createTable(DatabaseMapp databaseMapp);

    void deleteTable(String var1, String var2, String var3);

//    void createTable(ExcelData var1);
//
//    void alterTable(ExcelData var1, List<String> var2);
}