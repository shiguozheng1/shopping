package com.step.service;

import com.step.entity.primary.ProjectFnaExpenseInfo;

import java.util.List;

/**
 * Created by zhushubin  on 2019-10-24.
 * email:604580436@qq.com
 * 项目预算
 */
public interface ProjectFnaBudgetService {
    List<ProjectFnaExpenseInfo> findAll();
}
