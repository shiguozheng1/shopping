package com.step.service;

import com.step.entity.primary.HrmDepartment;
import com.step.entity.dto.FnaYearPeriodDto;
import com.step.entity.primary.form.*;
import com.step.entity.primary.vo.FnaBudgetInfoVo;
import com.step.exception.FnaException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zhushubin
 * @date 2019-08-29
 * email:604580436@qq.com
 * 预算管理
 */
public interface FnaBudgetService {
    /***
     * 判断是否启用预算控制
     * @return
     */
    Integer getCptCtrlSet();

    /***
     * 获取费用年Id
     * @return id
     * @param requestDate
     */
    Integer getFnaYearId(String requestDate);

    /***
     * 获取部门预算信息明细表
     * @return
     * @param bumen 部门
     * @param fnaYearId
     * @param kemu
     */
    Map<Integer,BigDecimal> getFnaBudgetInfoDetailMap(String bumen, int fnaYearId, String kemu);

    /***
     * 费用年开始时间及结算时间
     * @return list
     */
    List<FnaYearPeriodDto> getFnaYearPeriod(int fnaYearId);

    /***
     * 获取使用的费用
     * @param finalKemu 科目
     * @param finalBumen 部门
     * @param startdate 开始时间
     * @param enddate 结算时间
     * @param i 1 /0 使用 0 审批中
     * @return 金额
     */
    BigDecimal getFnaExpenseInfo(String finalKemu, String finalBumen, String startdate, String enddate, int i);

    /***
     * 获取分布信息
     * @param bumen
     * @return
     */
    public HrmDepartment getDepartment(String bumen);

    /***
     * 获取费用表
     * @param kemu 科目
     * @param bumen 部门
     * @param periodDateDtos 日期
     * @return
     */
    List getFnaExpenseInfoList(String kemu, String bumen, List<FnaYearPeriodDto> periodDateDtos);

    /***
     * 获取预算占用详情
     * @param budgetExecDate 预算执行时间
     * @param budgetAccountBizCode 预算科目业务编码
     * @param bumen 部门
     * @return FnaBudgetInfoVo
     */
    FnaBudgetInfoVo getFnaBudgetInfoByDepId(Date budgetExecDate,String budgetAccountBizCode, String bumen);

    /***
     * 判断是否开启预算校验
     * @return boolean
     */
    boolean isFnaController();

    /***
     * 预算处理，占用，冻结释放
     * @param fnaBudgetExecForm
     */
    List<BudgetExecEntrie> execBudget(FnaBudgetExecForm fnaBudgetExecForm) throws FnaException;

    /***
     * 获取最大的id
     * @return
     */
    Integer  getSequenceId();

    /***
     * 获取项目预算信息
     *
     * @param budgetExecDate  预算执行时间
     * @param code 业务实体预算信息
     * @return FnaBudgetInfoVo
     */
    FnaBudgetInfoVo getFnaBudgetInfoByProjectBizCode(Date budgetExecDate, String code);
}
