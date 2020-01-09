package com.step.strategy.org;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.step.entity.dto.MaycurCompany;
import com.step.entity.dto.SaveResultInfoDto;
import com.step.entity.dto.req.RequestData;
import com.step.entity.dto.res.ResponseCompany;
import com.step.entity.dto.res.ResponsePostData;
import com.step.entity.primary.HrmSubCompany;
import com.step.exception.OrgException;
import com.step.properties.MaycurProperties;
import com.step.service.*;
import com.step.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Slf4j
@Component("subCompany")
public class SubCompanyStrategy extends Strategy {
    @Autowired
    private HrmSubCompanyService hrmSubCompanyService;


    @Override
    public void invoke()  throws OrgException{
        Function<ResponseCompany, MaycurCompany> function = new Function<ResponseCompany, MaycurCompany>() {
            @Override
            public MaycurCompany apply(ResponseCompany rc) {
                MaycurCompany maycurCompany = new MaycurCompany();
                maycurCompany.setBusinessCode(rc.getBusinessCode());
                maycurCompany.setName(rc.getName());
                maycurCompany.setActive(rc.getEnabled());
                maycurCompany.setParentBizCode(rc.getParentBizCode());
                maycurCompany.setPrincipal(rc.getPrinciple());
                maycurCompany.setBaseCurrency(rc.getBaseCcy());
                return maycurCompany;
            }
        };
        List<MaycurCompany> modifyList = Lists.newArrayList();
        List<Map<String,String>> deleteCode = Lists.newArrayList();
        List errors =  Lists.newArrayList();
        try {
        HrmSubCompany hrmSubCompany = hrmSubCompanyService.findById(maycurProperties.getCompanyId());
        List<MaycurCompany> dbCompanys= hrmSubCompanyService
                .findChildren(hrmSubCompany,true,null).stream()
                .map(new Function<HrmSubCompany, MaycurCompany>() {
                    @Override
                    public MaycurCompany apply(HrmSubCompany hrmSubCompany) {
                        MaycurCompany maycurCompany = new MaycurCompany();
                        maycurCompany.setName(hrmSubCompany.getName());
                        maycurCompany.setBusinessCode(String.valueOf(hrmSubCompany.getId()));
                        maycurCompany.setActive(!hrmSubCompany.getCpCanceled());
                        if(hrmSubCompany.getParent()==null){
                            maycurCompany.setParentBizCode(maycurProperties.getStepBusinessCode());
                        }else{
                            maycurCompany.setParentBizCode(String.valueOf(hrmSubCompany.getParent().getId()));
                        }
                        return maycurCompany;
                    }
                }).collect(Collectors.toList());

            List<MaycurCompany> formatList = maycurService.formatPostData(token.getTokenId(), token.getEntCode(), function, new TypeReference<ResponsePostData<List<ResponseCompany>>>() {
            }, dbCompanys, maycurProperties.getSubDataUrl());
            IntStream.range(0, formatList.size())
                    .forEach(i->{
                        MaycurCompany   e = formatList.get(i);
                        if(e.getMethod() == "U" || e.getMethod() == "C"){
                            modifyList.add(e);
                        } else if(e.getMethod() == "D"&&!e.getBusinessCode().equals(maycurProperties.getStepBusinessCode())){
                            Map<String, String> map = new HashMap<>();
                            map.put("businessCode", e.getBusinessCode());
                            log.info("删除业务Code:{},name:{}",e.getKey(),e.getName());
                            deleteCode.add(map);
                        }
                    });
        }catch (Exception e){
            errors.add(e.getMessage());
            log.error(e.getMessage());
        }
        ResponsePostData<List<SaveResultInfoDto>> tmpSaveRet=restService.requestSave(token.getTokenId(), token.getEntCode(), new RequestData(modifyList), maycurProperties.getSaveSubUrl(),new TypeReference<ResponsePostData<List<SaveResultInfoDto>>>(){});
        ResponsePostData<List<SaveResultInfoDto>> tmpDeleteRet =restService.requestDelete(token.getTokenId(), token.getEntCode(), new RequestData(deleteCode), maycurProperties.getDeleteSubUrl(),  new TypeReference<ResponsePostData<List<SaveResultInfoDto>>>(){});
        if(tmpSaveRet!=null&&tmpSaveRet.getCode().equals("NACK")){
            errors.add(tmpSaveRet.getErrorData());
        }
        if(tmpDeleteRet!=null&&tmpDeleteRet.getCode().equals("NACK")){
            errors.add(tmpSaveRet.getErrorData());
        }
        if(errors!=null&&errors.size()>0){
            throw new OrgException(JsonUtils.toString(errors));
        }
    }
}
