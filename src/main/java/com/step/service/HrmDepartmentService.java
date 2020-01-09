package com.step.service;

import com.step.entity.primary.HrmDepartment;

import java.util.List;

/**
 * Created by user on 2019-04-18.
 */
public interface HrmDepartmentService {
    /***
     * 根据公司id 获取所有的部门
     * @param cids 公司id
     * @return list
     */
    List<HrmDepartment> findByCompanyIds(List<Long> cids);

    List<HrmDepartment> findAllPO();

}
