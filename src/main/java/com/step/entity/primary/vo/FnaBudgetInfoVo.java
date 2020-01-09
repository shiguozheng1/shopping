package com.step.entity.primary.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author zhushubin
 * @date 2019-08-29
 * email:604580436@qq.com
 * 每刻报销预算请求返回数据
 */
@Data
public class FnaBudgetInfoVo implements Serializable {
    private static final long serialVersionUID = 9079633927463670889L;
    /***
     * 预算编码
     */
    private String budgetCode;
    /***
     * 预算显示名
     */
    private String budgetName;
    /***
     * 预算单元币种
     */
    private String currency = "CNY";
    /***
     * 预算总额度
     */
    private String totalAmount;
    /***
     * 预算冻结中额度
     */
    private String freezedAmount;
    /***
     * 预算已使用额度
     */
    private String usedAmount;
    /***
     * 剩余可用额度
     */
    private String leftAmount;
    /***
     * 预算单元负责人工号
     */
    private String  principalEmployeeNo;
    /***
     * 预算允许超标范围
     */
    private String excessRatio = "0";
    /***
     * 警告阀值
     */
    private String warningThreshold ="0";
    private Integer orders;

}
