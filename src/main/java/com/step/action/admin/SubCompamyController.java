package com.step.action.admin;

import com.google.common.collect.Lists;
import com.step.entity.primary.FnaBudgetfeeType;
import com.step.entity.primary.HrmDepartment;
import com.step.entity.primary.HrmSubCompany;
import com.step.entity.primary.vo.FnaBudgetfeeTypeVo;
import com.step.service.HrmDepartmentService;
import com.step.service.HrmSubCompanyService;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-10-30.
 * email:604580436@qq.com
 */
@RestController("adminSubCompamyController")
@RequestMapping("/admin/subcompany")
public class SubCompamyController {
    @Autowired
    private HrmSubCompanyService hrmSubCompanyService;
    @Autowired
    private HrmDepartmentService hrmDepartmentService;
    @GetMapping
    public Wrapper all(){
        List<HrmSubCompany> fnaBudgetfeeTypes=  hrmSubCompanyService.findAll();
        List<Long> cids = Lists.newArrayList();
        List<FnaBudgetfeeTypeVo> rets= fnaBudgetfeeTypes.stream().filter(e->{
            if(e.getCanceled()==null){
                return true;
            }
            return !e.getCanceled();
        }).map(e->{
            FnaBudgetfeeTypeVo fnaBudgetfeeTypeVo = new FnaBudgetfeeTypeVo();
            //fnaBudgetfeeTypeVo.setDisabled(true);
            fnaBudgetfeeTypeVo.setId(e.getCpId());
            fnaBudgetfeeTypeVo.setPId(e.getParent()==null?null:e.getParent().getCpId());
            fnaBudgetfeeTypeVo.setTitle(e.getName());
            fnaBudgetfeeTypeVo.setValue(e.getId()+"");
            fnaBudgetfeeTypeVo.setSelectable(false);
            fnaBudgetfeeTypeVo.setDisableCheckbox(true);
            cids.add(e.getId());
            return fnaBudgetfeeTypeVo;
        }).collect(Collectors.toList());
        return WrapMapper.ok(rets);
    }
}
