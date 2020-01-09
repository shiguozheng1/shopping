package com.step.entity.primary;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhushubin  on 2019-09-24.
 * email:604580436@qq.com
 * OA 部门扩展字段
 */
@Data
@Entity
@Table(name="HrmDepartmentDefined")
public class HrmDepartmentDefined {
    @Id
    private Long id;
    @Column(name="deptid")
    private Long deptId;
    /***
     * u8 账套
     */
    private String u8zt;
    /***
     * u8 部门编码
     */
    private String u8bm;
    /***
     * 部门辅助人
     */
    private String manager;
}
