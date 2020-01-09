package com.step.service;

import com.step.entity.primary.HrmDepartmentDefined;
import com.step.entity.primary.vo.CostTrackingInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by zhushubin  on 2019-09-24.
 * email:604580436@qq.com
 * 辅助核算
 */
public interface CostTrackingService {
    /***
     * 根据公司ID 获取 辅助预算信息
     * @param cid 公司id
     * @return list
     */
    List<CostTrackingInfo> findCostTrackingByCompanyId(Long cid);

    /**
     * 根据公司ID 集合 获取U8
     * @param cids
     * @return
     */
    Map<Long,HrmDepartmentDefined> findCostTrackingByCompanyIds(List<Long> cids);
}
