package com.step.entity.primary;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 * 费用表
 * SET IDENTITY_INSERT FnaExpenseInfo OFF 需开启这个模式
 */
@Entity
@Table(name = "FnaExpenseInfo")
@Data
public class FnaExpenseInfo {
    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    /***
     * 组织id
     */
    private Integer organizationid;
    /***
     * 组织类型:分部1，部门2，个人3
     */
    private Integer organizationtype;
    /***
     * 费用发烧日期
     */
    @Column(length = 10)
    private String occurdate;
    /***
     * 金额
     */
    private BigDecimal amount;
    /***
     * 科目
     */
    private Integer subject;
    /***
     * 生效1 审批中0
     */
    private Integer status;
    /***
     * 付款1 报销2 冲销0
     */
    private Integer type;
    /***
     * 相关请求
     */
    private Integer relatedprj;
    /***
     * 相关客户
     */
    private Integer relatedcrm;
    /***
     * 凭证号
     */
    private String debitremark;
    /***
     * 说明
     */
    private String description;
    /***
     * 请求ID
     */
    private Integer requestid;
    private String shareRequestId;
    //修改数据库
    @Column(length = 255)
    private String guid;

    private Integer requestidDtlId;
    private Integer sourceDtlNumber;
    private Integer sourceRequestid;
    private Integer sourceRequestidDtlId;
    private Integer budgetperiods;
    private Integer budgetperiodslist;
    @Column(length = 10)
    private String OCCURDATEold;
    @Column(name="isBudgetAutoMove")
    private Integer isBudgetAutoMove;
    private Integer isBudgetAutoMoveByMinusAmt;
    private Integer sfbxwc;


}
