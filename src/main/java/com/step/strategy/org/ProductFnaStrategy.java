package com.step.strategy.org;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.step.entity.dto.req.RequestData;
import com.step.entity.dto.res.ResponsePostData;
import com.step.entity.primary.ProjectFnaExpenseInfo;
import com.step.entity.primary.vo.CostTrackingInfo;
import com.step.exception.OrgException;
import com.step.properties.MaycurProperties;
import com.step.service.MaycurService;
import com.step.service.ProjectFnaBudgetService;
import com.step.service.RestService;
import com.step.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-10-24.
 * email:604580436@qq.com
 * 项目预算辅助核算
 */
@Component("productFna")
public class ProductFnaStrategy extends Strategy  {
    @Autowired
    private ProjectFnaBudgetService projectFnaBudgetService;

    @Override
    public void invoke() throws OrgException {
        List<ProjectFnaExpenseInfo> projectFnaExpenseInfos =projectFnaBudgetService.findAll();
       List<CostTrackingInfo>  costTrackingInfos =projectFnaExpenseInfos.stream().filter(e->{

           if(e.getKg()!=null&&e.getKg().equals(ProjectFnaExpenseInfo.ProjectStatus.CLOSE)){
               return false;
           }
           if(StringUtils.isNotEmpty(e.getSn())){
               return true;
           }
           return false;
       }).map(e->{
           CostTrackingInfo trackingInfo = new CostTrackingInfo();
           trackingInfo.setName(e.getName()+"["+e.getSn()+"]");
           trackingInfo.setBusinessCode(e.getSn());
           trackingInfo.setVisibility("OPEN");
           trackingInfo.setPrincipal(e.getPrincipal()==null?"":e.getPrincipal()+"");
           return trackingInfo;
       }).collect(Collectors.toList());
        RequestData requestData = new RequestData(costTrackingInfos);
        List result =  Lists.newArrayList();


        ResponsePostData<Object> ret= restService.requestSave(token.getTokenId(), token.getEntCode(),requestData,maycurProperties.getCostTrackingSaveUrl(),
                new TypeReference<ResponsePostData<Object>>(){});
        if(ret!=null&&!ret.getCode().equals("ACK")){
            result.add(ret.getErrorData());
        }
        if(result!=null&&result.size()>0){
            throw new OrgException(JsonUtils.toString(result));
        }
    }
}
