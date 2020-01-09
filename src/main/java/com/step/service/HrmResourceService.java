package com.step.service;

import com.step.entity.primary.HrmResource;

import java.util.List;

/**
 * Created by user on 2019-04-18.
 */
public interface HrmResourceService {
    List<HrmResource> findByDepartmentIds(List<Long> depids);
    List<HrmResource> findByCompanyIds(List<Long> cids);
    List<HrmResource> findAll();

    /***
     * 获取能够同步的人员ID
     * @return
     */
    List<Long> findCusFielddata();

    /**
     * 根据id获取人员
     * @param ids
     * @return
     */
    List<HrmResource> findByIds(List<Long> ids);
}
