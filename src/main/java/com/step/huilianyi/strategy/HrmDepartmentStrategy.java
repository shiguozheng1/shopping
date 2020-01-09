package com.step.huilianyi.strategy;

import com.step.exception.OrgException;
import com.step.service.HrmDepartmentService;
import org.springframework.stereotype.Component;

/**
 * Created by zhushubin  on 2019-12-09.
 * email:604580436@qq.com
 * 同步汇联易部门
 */
@Component("hlHrmDepartmentStrategy")
public class HrmDepartmentStrategy extends Strategy {
    private HrmDepartmentService hrmDepartmentService;


   public HrmDepartmentStrategy(){
       this.setOrder(1);
   }
    @Override
    public void invoke() throws OrgException {
        System.out.println("################");
    }

}
