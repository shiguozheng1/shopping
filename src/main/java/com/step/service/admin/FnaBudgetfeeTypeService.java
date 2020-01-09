package com.step.service.admin;

import com.step.entity.primary.FnaBudgetfeeType;

import java.util.List;

/**
 * Created by zhushubin  on 2019-10-30.
 * email:604580436@qq.com
 * 预算科目
 */
public interface FnaBudgetfeeTypeService {
    /***
     * 查找所有的科目
     * @return
     */
    List<FnaBudgetfeeType> findAll();
}
