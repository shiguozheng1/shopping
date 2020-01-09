package com.step.entity.primary.form;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author zhushubin
 * @date 2019-08-29
 * email:604580436@qq.com
 * 每刻报销 预算提交信息
 */
@Data
public class FnaBudgetSearchForm implements Serializable{
    private static final long serialVersionUID = -2921207163026339622L;
    /***
     * 预算执行时间
     */
   private Date budgetExecDate;
    /***
     * 预算科目业务编码
     */
   private String budgetAccountBizCode;
    /***
     * 部门业务编码
     */
   private List<String> deptBizCodes;
    /***
     * 业务实体编码
     */
   private List<String> subsidiaryBizCodes;
    /***
     * 项目编码
     */
   private List<String> projectBizCodes;
    /***
     * 员工工号
     */
   private String employeeNo;



}
