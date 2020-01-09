package com.step.strategy.org;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.step.entity.dto.req.RequestData;
import com.step.entity.dto.res.ResponsePostData;
import com.step.entity.primary.vo.CostTrackingInfo;
import com.step.entity.primary.vo.CostTrackingRespResult;
import com.step.exception.OrgException;
import com.step.properties.MaycurProperties;
import com.step.service.CostTrackingService;
import com.step.service.MaycurService;
import com.step.service.RestService;
import com.step.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/**
 * Created by zhushubin  on 2019-09-24.
 * email:604580436@qq.com
 * 同步辅助核算
 */
@Component("costTracking")
public class CostTrackingStrategy extends Strategy  {
    @Autowired
    private CostTrackingService costTrackingService;


    @Override
    public void invoke() throws OrgException {
      Long cid=  maycurProperties.getCompanyId();
      List<CostTrackingInfo> costTrackingInfos =costTrackingService.findCostTrackingByCompanyId(cid);
        Function<CostTrackingRespResult, CostTrackingInfo> function = new Function<CostTrackingRespResult, CostTrackingInfo>() {
            @Override
            public CostTrackingInfo apply(CostTrackingRespResult ctr) {
                CostTrackingInfo trackingInfo = new CostTrackingInfo();
                trackingInfo.setName(ctr.getCostTrackingItemName());
                trackingInfo.setBusinessCode(ctr.getCostTrackingItemBizCode());
                return trackingInfo;
            }
        };



        RequestData requestData = new RequestData(costTrackingInfos);
        List result =  Lists.newArrayList();

        try{
            ResponsePostData<Object> ret= restService.requestSave(token.getTokenId(), token.getEntCode(),requestData,maycurProperties.getCostTrackingSaveUrl(),
                    new TypeReference<ResponsePostData<Object>>(){});
            if(ret!=null&&!ret.getCode().equals("ACK")){
                result.add(ret.getErrorData());
            }
        }catch (Exception e){
            e.printStackTrace();
            result.add(e.getMessage());
        }
        if(result!=null&&result.size()>0){
            throw new OrgException(JsonUtils.toString(result));
        }

    }
}
