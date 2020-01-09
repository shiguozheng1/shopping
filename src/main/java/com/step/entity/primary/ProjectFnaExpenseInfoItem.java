package com.step.entity.primary;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.id.IncrementGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhushubin  on 2019-10-16.
 * email:604580436@qq.com
 * 项目预算信息
 */
@Entity
@Table(name = "z_project_fna_expenseInfo_item")
@Data
public class ProjectFnaExpenseInfoItem {
    /***
     * 预算编码
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="budget_code")
    private String budgetCode;
    /***
     * 项目预算Id
     */
    @Column(name="pid")
    private Integer  pid;
    /***
     * 用户ID
     */
    @Column(name="uid")
    private Integer  uid;
    /***
     * 创建时间
     */
    @Column(name="create_time")
    private Date createTime;
    /***
     * 提交金额
     */
    @Column(name="old_price")
    private BigDecimal oldPrice;
    /***
     * 修改后使用的金额
     */
    @Column(name="real_price")
    private BigDecimal realPrice;
    @Column(name="opt")
    private String  opt;

    public ProjectFnaExpenseInfoItem() {
    }

    /***
     *
     * @param budgetCode 预算编码
     * @param amount 金额
     * @param pid  预算项目id
     */
    public ProjectFnaExpenseInfoItem(@NotBlank(message = "budgetCode cannot be null") String budgetCode, BigDecimal amount, Integer pid) {
        this.budgetCode = budgetCode;
        this.realPrice = amount;
        this.pid = pid;
        this.createTime = new Date();
    }
}
