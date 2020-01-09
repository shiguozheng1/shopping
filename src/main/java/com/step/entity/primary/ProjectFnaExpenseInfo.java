package com.step.entity.primary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by zhushubin  on 2019-10-16.
 * email:604580436@qq.com
 * 项目预算信息
 */
@Entity
@Table(name = "uf_zwxxmys")
@Data
public class ProjectFnaExpenseInfo {
   public enum ProjectStatus{
        CLOSE("关"),OPEN("开");
        public String text;
        ProjectStatus(String  text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
    @Id
    @Column(name = "id")
    @JsonIgnore
    private Integer id;
    @Column(name = "requestId")
    @JsonIgnore
    private Integer requestId;
    @JsonIgnore
    private String u8zt;
    /***
     * 公司名称
     */
    @Column(name = "gsmc")
    @JsonIgnore
    private String subcompanyName;
    /***
     * 公司Id
     */
    @Column(name = "oagsbm")
    @JsonIgnore
    private String subCompanyId;
    /***
     * 部门id
     */
    @Column(name = "oabmid")
    @JsonIgnore
    private String departmentId;
    /***
     * 事业部名称
     */
    @Column(name = "u8syb")
    @JsonIgnore
    private String businessDepartmentName;
    /***
     * 项目编号
     */
    @Column(name = "xmh")
    private String sn;
    /***
     * 项目名称
     */
    @Column(name = "xmmc")
    private String name;

    /***
     * 项目类型
     */
    @Column(name = "xmlx")
    @JsonIgnore
    private String type;
    /***
     * 项目状态表
     */
    @Column(name = "xmztb")
    private String status;
    /***
     * 总预算
     */
    @Column(name = "zys")
    private BigDecimal totalPrice;
    /***
     * 已使用预算
     */
    @Column(name = "ysyfy")
    private BigDecimal usedPrice;
    /***
     * 冻结的预算
     */
    @Column(name = "djys")
    private BigDecimal freezedPrice;
    /***
     * 剩余预算
     */
    @Column(name = "syys")
    private BigDecimal residuedPrice;
    @Column(name = "projectmanager")
    private Long principal;
    @JsonIgnore
    private Integer formmodeid;
    @JsonIgnore
    private Integer modedatacreater;
    @JsonIgnore
    private Integer modedatacreatertype;
    @JsonIgnore
    private String modedatacreatedate;
    @JsonIgnore
    private String modedatacreatetime;
    /***
     * 项目开关
     */
    @JsonIgnore
    @Enumerated(EnumType.ORDINAL)
    private ProjectStatus kg;


}
