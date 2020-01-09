package com.step.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.step.entity.primary.HrmDepartment;
import com.step.entity.primary.HrmDepartmentDefined;
import com.step.entity.primary.vo.CostTrackingInfo;
import com.step.repository.primary.HrmDepartmentDefinedRepository;
import com.step.repository.primary.HrmDepartmentRepository;
import com.step.service.CostTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-09-24.
 * email:604580436@qq.com
 */
@Service
@Transactional("transactionManagerPrimary")
@Slf4j
public class CostTrackingServiceImpl implements CostTrackingService {
    @Autowired
    private HrmDepartmentDefinedRepository hrmDepartmentDefinedRepository;
    @Autowired
    private HrmDepartmentRepository hrmDepartmentRepository;
    @Override
    public List<CostTrackingInfo> findCostTrackingByCompanyId(Long cid) {
        List<HrmDepartment> hrmDepartments= hrmDepartmentRepository.findChildren(Lists.newArrayList(cid));
        Assert.notEmpty(hrmDepartments,"部门不允许为空");
        List<Long> depIds = hrmDepartments.stream().map(e->{
            return e.getId();
        }).collect(Collectors.toList());
        List<HrmDepartmentDefined> defineds= hrmDepartmentDefinedRepository.findByDeptIdIn(depIds);
        if(defineds !=null&&defineds.size()>0){

            List<CostTrackingInfo> costTrackingInfoList = Lists.newArrayList();
            defineds.stream().filter(e->{
                //过滤掉不合规的数据
                return StringUtils.isNotEmpty(e.getU8zt())&&StringUtils.isNotEmpty(e.getU8bm());
            }).forEach(e->{
                List<String> key = Splitter.on("#").trimResults()
                        .splitToList(e.getU8zt());
                List<String> value = Splitter.on("#").trimResults()
                        .splitToList(e.getU8bm());
                if(key !=null&&key.size()>0 && value!=null&&value.size()>0&&value.size()==key.size()){
                    for(int i=0;i<key.size();++i){
                        CostTrackingInfo trackingInfo = new CostTrackingInfo();
                        trackingInfo.setName(key.get(i));
                        trackingInfo.setBusinessCode(e.getId()+"_"+value.get(i));
                        costTrackingInfoList.add(trackingInfo);
                    }
                }else{
                    log.error("辅助预算名称跟值不匹配");
                }
            });
            return costTrackingInfoList;

        }

        return null;
    }

    @Override
    public  Map<Long,HrmDepartmentDefined> findCostTrackingByCompanyIds(List<Long> cids) {
        List<HrmDepartment> hrmDepartments= hrmDepartmentRepository.findChildren(cids);
        Assert.notEmpty(hrmDepartments,"部门不允许为空");
        List<Long> depIds = hrmDepartments.stream().map(e->{
            return e.getId();
        }).collect(Collectors.toList());
        List<HrmDepartmentDefined> defineds= hrmDepartmentDefinedRepository.findByDeptIdIn(depIds);
        if(defineds !=null&&defineds.size()>0){
            Map<Long,HrmDepartmentDefined> u8bmMap= Maps.newHashMap();
            defineds.stream().forEach(e->{
                u8bmMap.put(e.getDeptId(),e);
            });
            return u8bmMap;

        }

        return null;
    }
}
