package com.step.action;

import com.google.common.collect.Lists;
import com.step.entity.dto.res.ResponseSearchBudget;
import com.step.entity.primary.form.BudgetExecEntrie;
import com.step.entity.primary.form.FnaBudgetExecForm;
import com.step.entity.primary.form.FnaBudgetSearchForm;
import com.step.entity.primary.vo.FnaBudgetExecResult;
import com.step.entity.primary.vo.FnaBudgetInfoVo;
import com.step.exception.FnaException;
import com.step.exception.OrgException;
import com.step.properties.MaycurProperties;
import com.step.service.*;
import com.step.strategy.org.*;
import com.step.utils.JsonUtils;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import io.jsonwebtoken.lang.Assert;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 每刻报销
 * shigz
 * 2019/8/28
 */
@RestController
@RequestMapping("/maycur")
@Slf4j(topic = "error")
public class MaycurController extends BaseController {
    @Autowired
    private FnaBudgetService fnaBudgetService;

    @Autowired
    private StrategyContext context;

    @GetMapping(value = "/org/sync")
    public Wrapper syncOrganization(String unit) throws Exception {
        Strategy strategy = context.getStrategy(unit);
        Assert.notNull(strategy);
        Object errObj=null;
        try {
            strategy.doSync();
        } catch (OrgException e) {
            errObj=e.getErrors();
            e.printStackTrace();
        }
        if (errObj!=null) {
            return WrapMapper.wrap(Wrapper.ResponseType.ERROR, "maycur", errObj);
        }
        return WrapMapper.ok();
    }


    @PostMapping("search/budget")
    public ResponseSearchBudget searchBudget(@RequestBody FnaBudgetSearchForm fnaBudgetForm) {
        log.error(JsonUtils.toString(fnaBudgetForm));
        List<FnaBudgetInfoVo> result = new ArrayList<FnaBudgetInfoVo>();
        //部门预算
        List<String> deptBizCodes = fnaBudgetForm.getDeptBizCodes();
        FnaBudgetInfoVo fnaBudgetInfoVo = null;
        for (String dept : deptBizCodes) {
            fnaBudgetInfoVo = fnaBudgetService.getFnaBudgetInfoByDepId(fnaBudgetForm.getBudgetExecDate(),
                    fnaBudgetForm.getBudgetAccountBizCode(), dept);

            if(fnaBudgetInfoVo!=null) {
                //获取部门预算
                fnaBudgetInfoVo.setOrders(0);
                result.add(fnaBudgetInfoVo);
            }
        }
        //项目预算
        List<String> subsidiaryBizCodes = fnaBudgetForm.getProjectBizCodes();
        if (subsidiaryBizCodes != null && subsidiaryBizCodes.size() > 0) {
            subsidiaryBizCodes.stream().forEach(code -> {
                FnaBudgetInfoVo budgetInfoVo = fnaBudgetService.getFnaBudgetInfoByProjectBizCode(fnaBudgetForm.getBudgetExecDate(), code);
                if (budgetInfoVo != null) {
                    budgetInfoVo.setOrders(1);
                    result.add(budgetInfoVo);
                }
            });
        }
        if(result!=null&&result.size()>1) {
            Collections.sort(result, Comparator.comparing(FnaBudgetInfoVo::getOrders).reversed());
        }
        ResponseSearchBudget responseSearchBudget = new ResponseSearchBudget(result);
        responseSearchBudget.setIsSuccess(true);
        return responseSearchBudget;
    }

    /**
     * 预算占用及释放
     *
     * @param fnaBudgetExecForm
     * @return
     */
    @PostMapping("exec/budget")
    public FnaBudgetExecResult execBudget(@RequestBody FnaBudgetExecForm fnaBudgetExecForm) {
        FnaBudgetExecResult result = new FnaBudgetExecResult();

        List<String> vRet = validater(fnaBudgetExecForm);
        if (vRet != null && vRet.size() > 0) {
            log.error(JsonUtils.toString(vRet));
            result.setSuccess(false);
            result.setBudgetExecEntries(fnaBudgetExecForm.getBudgetExecEntries());
            return result;
        }

        List<BudgetExecEntrie> errors = null;
        try {
            log.error(JsonUtils.toString(fnaBudgetExecForm));
            errors = fnaBudgetService.execBudget(fnaBudgetExecForm);
        } catch (FnaException e) {
            e.printStackTrace();
            if (e.getMessage().equals("记录已存在不允许重复提交")) {
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setBudgetExecEntries(errors);
            }
            //如果已经存在就默认成功

            return result;

        }
        if (errors != null && errors.size() > 0) {
            result.setSuccess(false);
            result.setBudgetExecEntries(errors);
        } else {
            result.setSuccess(true);
        }
        return result;
    }

}

