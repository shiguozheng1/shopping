package com.step.strategy.org;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.step.entity.dto.MaycurEmployee;
import com.step.entity.dto.req.RequestData;
import com.step.entity.dto.res.ResponseGetData;
import com.step.entity.dto.res.ResponsePostData;
import com.step.entity.dto.res.employee.EmpDepInfoDto;
import com.step.entity.dto.res.employee.ResultEmpInfoDto;
import com.step.entity.primary.HrmResource;
import com.step.entity.primary.HrmSubCompany;
import com.step.entity.primary.vo.ErrorDataInfo;
import com.step.exception.OrgException;
import com.step.properties.MaycurProperties;
import com.step.service.*;
import com.step.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component("resource")
public class HrmResourceStrategy extends Strategy {
    @Autowired
    private HrmSubCompanyService hrmSubCompanyService;
    @Autowired
    private HrmResourceService hrmResourceService;

    @Override
    public void invoke()throws OrgException {


        Function<MaycurEmployee, MaycurEmployee> function = new Function<MaycurEmployee, MaycurEmployee>() {
            @Override
            public MaycurEmployee apply(MaycurEmployee me) {
                return me;
            }
        };
        List<MaycurEmployee> modifyList = Lists.newArrayList();
        List<Map<String, String>> deleteCode = Lists.newArrayList();
        List<Map<String, String>> deleteDep = Lists.newArrayList();
        List errors = Lists.newArrayList();
        try {
            HrmSubCompany hrmSubCompany = hrmSubCompanyService.findById(maycurProperties.getCompanyId());
            List<Long> cids = hrmSubCompanyService.findChildren(hrmSubCompany, true, null).stream().map(e -> e.getId()).collect(Collectors.toList());
            List<Long> filterIds = hrmResourceService.findCusFielddata();

            List<MaycurEmployee> dbEmpolyees = hrmResourceService.findByCompanyIds(cids).stream()
//                .filter(e->{
//            if(filterIds.contains(e.getId())){
//                return true;
//            }
//            return false;
//        })
                    .map(new Function<HrmResource, MaycurEmployee>() {

                        @Override
                        public MaycurEmployee apply(HrmResource hr) {

                            MaycurEmployee maycurEmployee = new MaycurEmployee();
                            EmpDepInfoDto empDepInfoDto = new EmpDepInfoDto();
                            maycurEmployee.setMobile(hr.getMobile());
                            maycurEmployee.setEmail(hr.getEmail());
                            maycurEmployee.setDefaultSubsidiaryBizCode(String.valueOf(hr.getHrmSubCompany().getId()));
                            maycurEmployee.setName(hr.getLastname());
                            maycurEmployee.setEmployeeId(String.valueOf(hr.getId()));
                            maycurEmployee.setCustField1(hr.getWorkcode());
                            maycurEmployee.setRank(String.valueOf(hr.getSeclevel()));
                            maycurEmployee.setPosition(String.valueOf(hr.getSeclevel()));
                            if (hr.getManager() != null) {
                                empDepInfoDto.setManagerId(String.valueOf(hr.getManager().getId()));
                            } else {
                                log.error("上级不存在{}", hr.toString());
                            }
                            if (hr.getHrmDepartment() != null) {
                                empDepInfoDto.setDepartmentBizCode(String.valueOf(hr.getHrmDepartment().getId()));
                            } else {
                                log.error("部门不存在{}", hr.toString());
                            }
                            maycurEmployee.setDepartments(Lists.newArrayList(empDepInfoDto));
                            return maycurEmployee;
                        }
                    }).collect(Collectors.toList());


            List<MaycurEmployee> formatList = maycurService.formatGetData(token.getTokenId(), token.getEntCode(), function, new TypeReference<ResponseGetData<MaycurEmployee>>() {
                    },
                    dbEmpolyees, maycurProperties.getFindEmployeeUrl(), null);

            IntStream.range(0, formatList.size())
                    .forEach(i -> {
                        MaycurEmployee e = formatList.get(i);
                        if (e.getMethod().equals("U") || e.getMethod().equals("C")) {
                            modifyList.add(e);
                        } else if (e.getMethod().equals("D")) {
                            Map<String, String> map;
                            if (!maycurProperties.getAdministrator().contains(e.getEmployeeId()) && e.getUsable()) {
                                map = new HashMap<>();
                                map.put("employeeId", e.getEmployeeId());
                                if (StringUtils.isEmpty(e.getCustField2()) || (e.getCustField2() != null && !e.getCustField2().equals("Y"))) {
                                    deleteCode.add(map);
                                    log.info("离职人员Code:{},name:{}", e.getKey(), e.getName());
                                    if (e.getDepartments() != null && e.getDepartments().size() > 0) {
                                        map = new HashMap<>();
                                        log.info("离职人员删除部门:{},name:{}", e.getKey(), e.getName());
                                        map.put("employeeId", e.getEmployeeId());
                                        map.put("departmentBizCode", e.getDepartments().get(0).getDepartmentBizCode());
                                        deleteDep.add(map);
                                    }
                                }

                            }
                        }
                    });
        } catch (Exception e) {
            errors.add(e.getMessage());
            log.error(e.getMessage());
        }
        if (!modifyList.isEmpty()) {
            Map<String, Object> modifyEmp = Maps.newHashMap();
            Map<String, String> modifyMap = Maps.newHashMap();
            modifyList.stream().forEach(e -> {
                modifyMap.put(e.getKey(), e.getName());
            });
            modifyEmp.put("modifySuccess", modifyMap);
            List<List<MaycurEmployee>> parts = Lists.partition(modifyList, 300);
            parts.stream().forEach(list -> {
                ResponsePostData<List<ResultEmpInfoDto>> tmpSaveRet = restService.requestSave(token.getTokenId(), token.getEntCode(), new RequestData(list), maycurProperties.getSaveEmployeeUrl(),
                        new TypeReference<ResponsePostData<List<ResultEmpInfoDto>>>() {
                        });
                if (tmpSaveRet != null && !tmpSaveRet.getCode().equals("ACK")) {
                    List<ErrorDataInfo> errorList = formatErrorData(tmpSaveRet);
                    errorList.stream().forEach(e -> {
                        modifyMap.remove(e.getId());
                    });
                    modifyEmp.put("modifyError", errorList);
                    errors.add(modifyEmp);
                }
            });
        }
        if (!deleteCode.isEmpty()) {
            ResponsePostData<Object> tmpDisableRet = restService.requestSave(token.getTokenId(), token.getEntCode(), new RequestData(deleteCode), maycurProperties.getDisableEmployeeUrl(),
                    new TypeReference<ResponsePostData<Object>>() {
                    });
            Map<String, Object> deleteMap = Maps.newHashMap();
            deleteMap.put("deleteSuccess", deleteCode);
            if (tmpDisableRet != null && !tmpDisableRet.getCode().equals("ACK")) {
                List<ErrorDataInfo> errorList = formatErrorData(tmpDisableRet);
                errorList.stream().forEach(e -> {
                    deleteMap.remove(e.getId());
                });
                deleteMap.put("deleteError", errorList);
            }
        ResponsePostData<List<ResultEmpInfoDto>> tmpDeleteRet = restService.requestDelete(token.getTokenId(), token.getEntCode(), new RequestData(deleteDep), maycurProperties.getDeleteEmpDepUrl(), new TypeReference<ResponsePostData<List<ResultEmpInfoDto>>>() {
        });
        if (tmpDeleteRet != null && !tmpDeleteRet.getCode().equals("ACK")) {
            List<ErrorDataInfo> errorList = formatErrorData(tmpDeleteRet);
            deleteMap.put("deleteError", errorList);
        }
        errors.add(deleteMap);
    }
        if(errors!=null&&errors.size()>0){
            throw new OrgException("failure",errors);
        }
    }

    private List<ErrorDataInfo> formatErrorData(ResponsePostData res){
        List<Long> errorIds=new ArrayList<>();
        List<ErrorDataInfo> errorObj= Lists.newArrayList();
        Object obj= res.getErrorData();
        Map<String,String> error=null;
        if(obj!=null&&obj instanceof Map ){
            error=(Map<String,String>)res.getErrorData();
        }
        for(String key:error.keySet()){
            errorIds.add(Long.valueOf(key));
        }
        List<HrmResource> errorRes = hrmResourceService.findByIds(errorIds);
        ErrorDataInfo info =null;
        if(!errorRes.isEmpty()){
            for(HrmResource hrm :errorRes){
                info=new ErrorDataInfo();
                info.setId(String.valueOf(hrm.getId()));
                info.setMessage(error.get(String.valueOf(hrm.getId())));
                info.setLoginId(hrm.getLoginid());
                info.setLastname(hrm.getLastname());
                errorObj.add(info);
            }
        }
        return errorObj;
    }
}
