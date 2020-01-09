package com.step.huilianyi;

/**
 * Created by zhushubin  on 2019-12-09.
 * email:604580436@qq.com
 */

import com.step.action.BaseController;
import com.step.exception.OrgException;
import com.step.huilianyi.strategy.HrmDepartmentStrategy;
import com.step.huilianyi.strategy.Strategy;
import com.step.huilianyi.strategy.StrategyContext;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 汇联易报销系统
 * shigz
 * 2019/8/28
 */
@RestController
@RequestMapping("/hl")
@Slf4j(topic = "huilianyi")
public class HLController  extends BaseController{
    @Autowired
    private StrategyContext strategyContext;
    /***
     * 同步部门
     * @return
     */
    @GetMapping("/org/department")
    public Wrapper syncDepartment(){

        Strategy strategy = strategyContext.getStrategy("hlHrmDepartmentStrategy");
        Assert.notNull(strategy);
        String errMsg="";
        try {
            strategy.invoke();
        } catch (OrgException e) {
            errMsg=e.getMessage();
            e.printStackTrace();
        }
        if (StringUtils.isNotEmpty(errMsg)) {
        return WrapMapper.wrap(Wrapper.ResponseType.ERROR,"huilianyi",errMsg);
    }
    return WrapMapper.ok();
}
}
