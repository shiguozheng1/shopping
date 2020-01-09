package com.step.strategy.org;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.step.entity.dto.MaycurDepartment;
import com.step.entity.dto.SaveResultInfoDto;
import com.step.entity.dto.req.RequestData;
import com.step.entity.dto.res.ResponseDep;
import com.step.entity.dto.res.ResponsePostData;
import com.step.entity.primary.HrmDepartment;
import com.step.entity.primary.HrmDepartmentDefined;
import com.step.entity.primary.HrmSubCompany;
import com.step.exception.OrgException;
import com.step.properties.MaycurProperties;
import com.step.service.*;
import com.step.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Slf4j
@Component("department")
public class HrmDepartmentStrategy extends Strategy {
    @Autowired
    private HrmSubCompanyService hrmSubCompanyService;
    @Autowired
    private HrmDepartmentService hrmDepartmentService;
    @Autowired
    private CostTrackingService costTrackingService;

    @Override
    public void invoke() throws OrgException {

        Function<ResponseDep, MaycurDepartment> function = new Function<ResponseDep, MaycurDepartment>() {
            @Override
            public MaycurDepartment apply(ResponseDep rp) {
                MaycurDepartment maycurDepartment =new MaycurDepartment();
                maycurDepartment.setName(rp.getName());
                maycurDepartment.setBusinessCode(rp.getBusinessCode());
                maycurDepartment.setActive(rp.getEnabled());
                maycurDepartment.setParentBizCode(rp.getParentBizCode());
                maycurDepartment.setPrincipal(rp.getPrincipleId());
                maycurDepartment.setCostCenterCode(rp.getCostCenterCode());
                maycurDepartment.setSubsidiaryBizCodes(rp.getSubsidiaryBizCodes());
                maycurDepartment.setDirectSubsidiaryBizCode(rp.getDirectSubsidiaryBizCode());
                return maycurDepartment;
            }
        };
        List<MaycurDepartment> modifyList =Lists.newArrayList();
        List<Map<String,String>> deleteCode = Lists.newArrayList();
        List errors =  Lists.newArrayList();
        try{
        HrmSubCompany hrmSubCompany = hrmSubCompanyService.findById(maycurProperties.getCompanyId());
        List<HrmSubCompany> dbCompanys= hrmSubCompanyService.findChildren(hrmSubCompany,true,null);
        List<Long> cids= Lists.newArrayList();
        if(!dbCompanys.isEmpty()) {
            dbCompanys.stream().forEach(e -> cids.add(e.getId()));
        }

        //获取u8 编码及部门负责人
        Map<Long,HrmDepartmentDefined> u8bmMap = costTrackingService.findCostTrackingByCompanyIds(cids);
        List<MaycurDepartment> dbDepartments = hrmDepartmentService.findByCompanyIds(cids).stream().
                map(new Function<HrmDepartment, MaycurDepartment>() {
                    @Override
                    public MaycurDepartment apply(HrmDepartment hrmDepartment) {
                        MaycurDepartment maycurDepartment=new MaycurDepartment();
                        maycurDepartment.setName(hrmDepartment.getName());
                        maycurDepartment.setBusinessCode(String.valueOf(hrmDepartment.getId()));
                        if(hrmDepartment.getHrmSubCompany()!=null) {
                            maycurDepartment.setDirectSubsidiaryBizCode(String.valueOf(hrmDepartment.getHrmSubCompany().getId()));
                        }
                        HrmDepartmentDefined hrmDepartmentDefined =u8bmMap.get(hrmDepartment.getId());
                        if(hrmDepartmentDefined !=null){
                            if(StringUtils.isNotEmpty((hrmDepartmentDefined.getU8bm()))){
                                maycurDepartment.setCostCenterCode(hrmDepartmentDefined.getU8bm());
                            }
                            if(StringUtils.isNotEmpty((hrmDepartmentDefined.getManager()))){
                                maycurDepartment.setPrincipal(hrmDepartmentDefined.getManager());
                            }

                        }

                        maycurDepartment.setActive(!hrmDepartment.getCpCanceled());
                        if(hrmDepartment.getParent()!=null){
                            maycurDepartment.setParentBizCode(String.valueOf(hrmDepartment.getParent().getId()));
                        }
                        return maycurDepartment;
                    }
                }).collect(Collectors.toList());


            List<MaycurDepartment> formatList = maycurService.formatPostData(token.getTokenId(), token.getEntCode(), function, new TypeReference<ResponsePostData<List<ResponseDep>>>(){},
                    dbDepartments,maycurProperties.getDeptDataUrl());
            IntStream.range(0, formatList.size())
                    .forEach(i->{
                        MaycurDepartment  e = formatList.get(i);
                        if(e.getMethod() == "U" || e.getMethod() == "C"){
                            modifyList.add(e);
                        } else if(e.getMethod() == "D"){
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
        ResponsePostData<List<SaveResultInfoDto>> tmpSaveRet = restService.requestSave(token.getTokenId(), token.getEntCode(),new RequestData(modifyList), maycurProperties.getSaveDeptUrl(),new TypeReference<ResponsePostData<List<SaveResultInfoDto>>>(){});
        ResponsePostData<List<SaveResultInfoDto>> tmpDeleteRet =restService.requestDelete(token.getTokenId(), token.getEntCode(), new RequestData(deleteCode), maycurProperties.getDeleteDeptUrl(),new TypeReference<ResponsePostData<List<SaveResultInfoDto>>>(){});

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
