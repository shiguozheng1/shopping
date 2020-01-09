package com.step.action.admin;

import com.step.entity.primary.FnaBudgetfeeType;
import com.step.entity.primary.vo.FnaBudgetfeeTypeVo;
import com.step.entity.secondary.Admin;
import com.step.entity.secondary.dto.AdminDto;
import com.step.service.admin.FnaBudgetfeeTypeService;
import com.step.service.admin.UserService;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-10-30.
 * email:604580436@qq.com
 * 科目查找
 */
@RestController("adminSubjectController")
@RequestMapping("/admin/subject")
public class SubjectController {
    @Autowired
    private FnaBudgetfeeTypeService service; //预算科目
    @GetMapping
    public Wrapper all(){
      List<FnaBudgetfeeType> fnaBudgetfeeTypes=  service.findAll();
      List<FnaBudgetfeeTypeVo> rets= fnaBudgetfeeTypes.stream().filter(e->{
          if(e.getArchive()==null){
              return true;
          }
          if(!e.getArchive().equals("1")){
              return true;
          }
          return false;
      }).map(e->{
         FnaBudgetfeeTypeVo fnaBudgetfeeTypeVo = new FnaBudgetfeeTypeVo();
         fnaBudgetfeeTypeVo.setId(e.getId());
         fnaBudgetfeeTypeVo.setPId(e.getParent()==null?null:e.getParent().getId());
         fnaBudgetfeeTypeVo.setTitle(e.getName());
         fnaBudgetfeeTypeVo.setValue(e.getId()+"");
         return fnaBudgetfeeTypeVo;
      }).collect(Collectors.toList());
        return WrapMapper.ok(rets);
    }
}
