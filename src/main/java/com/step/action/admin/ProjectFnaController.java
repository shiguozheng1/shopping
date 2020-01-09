package com.step.action.admin;

import com.google.common.collect.Lists;
import com.step.entity.primary.HrmSubCompany;
import com.step.entity.primary.ProjectFnaExpenseInfo;
import com.step.entity.primary.vo.FnaBudgetfeeTypeVo;
import com.step.service.ProjectFnaBudgetService;
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
 * 项目预算
 */
@RestController("adminProFnaController")
@RequestMapping("/admin/profna")
public class ProjectFnaController {
    @Autowired
    private ProjectFnaBudgetService projectFnaBudgetService;
    @GetMapping
    public Wrapper all(){
        List<ProjectFnaExpenseInfo> projectFnaExpenseInfos=  projectFnaBudgetService.findAll();
        List<Long> cids = Lists.newArrayList();
        List<FnaBudgetfeeTypeVo> rets= projectFnaExpenseInfos.stream().filter(e->{
            if(e.getKg()==null){
                return true;
            }
            return e.getKg().equals(ProjectFnaExpenseInfo.ProjectStatus.OPEN);
        }).map(e->{
            FnaBudgetfeeTypeVo fnaBudgetfeeTypeVo = new FnaBudgetfeeTypeVo();
            fnaBudgetfeeTypeVo.setId(Long.valueOf(e.getId()));
            fnaBudgetfeeTypeVo.setPId(null);
            fnaBudgetfeeTypeVo.setTitle(e.getName()+"["+e.getSn()+"]");
            fnaBudgetfeeTypeVo.setValue(e.getSn());
            return fnaBudgetfeeTypeVo;
        }).collect(Collectors.toList());
        return WrapMapper.ok(rets);
    }
}
