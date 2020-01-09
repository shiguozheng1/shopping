package com.step.action.admin;

import com.google.common.collect.Lists;
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
@RestController("adminDepartmentController")
@RequestMapping("/admin/department")
public class DepartmentController {
    @Autowired
    private HrmDepartmentService hrmDepartmentService;
    @GetMapping
    public Wrapper list(Long cid){
        if(cid==null){
            return WrapMapper.error("公司不允许为空");
        }
        List<HrmDepartment> hrmDepartments = hrmDepartmentService.findByCompanyIds(Lists.newArrayList((cid-1000)));
        List<FnaBudgetfeeTypeVo> rets= hrmDepartments.stream().filter(e->{
            if(e.getCanceled()==null){
                return true;
            }
            return !e.getCanceled();
        }).map(e->{
            FnaBudgetfeeTypeVo fnaBudgetfeeTypeVo = new FnaBudgetfeeTypeVo();
            fnaBudgetfeeTypeVo.setId(e.getId());
            if(e.getParent()==null||e.getParent().getId().equals("0")){
                fnaBudgetfeeTypeVo.setPId(cid);
            }else {
                fnaBudgetfeeTypeVo.setPId(e.getParent() == null ? null : e.getParent().getId());
            }
            fnaBudgetfeeTypeVo.setTitle(e.getName());
            fnaBudgetfeeTypeVo.setValue(e.getId()+"");
            return fnaBudgetfeeTypeVo;
        }).collect(Collectors.toList());
        return WrapMapper.ok(rets);
    }
}
