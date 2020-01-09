package com.step.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * shigz
 * 2019/9/2
 **/
@Data
@JsonIgnoreProperties({"method","modifyName"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaycurDepartment extends DifferEntity{
    private String name; //部门名称
    private String nameEn;//部门英文名称
    private String businessCode;//部门编码
    private String costCenterCode="";//成本中心编码
    private List<String> subsidiaryBizCodes;//共享部门的业务实体编码
    private String directSubsidiaryBizCode;//直属业务实体编码
    private String principal;//部门负责人工号
    private String parentBizCode="";//上级部门编码
    private String childrenSubsidiaryOperation="";//如何处理子部门共享业务实体，
    private Boolean active;
    private String method;//update 或者 create
    private Object modifyName;
@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MaycurDepartment maycurDepartment = (MaycurDepartment) o;

        return new EqualsBuilder()
                .append(businessCode, maycurDepartment.businessCode)
                .append(name, maycurDepartment.name)
                .append(parentBizCode,maycurDepartment.parentBizCode)
                .append(principal,maycurDepartment.principal)
                .append(costCenterCode,maycurDepartment.costCenterCode)
                //@TODO 修改部门一级重名bug
                //.append(subsidiaryBizCodes,maycurDepartment.subsidiaryBizCodes)
                .append(directSubsidiaryBizCode,maycurDepartment.directSubsidiaryBizCode)
                .append(active,maycurDepartment.active)
                 .isEquals();

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(businessCode)
                .append(name)
                .append(parentBizCode)
                .append(principal)
                .append(costCenterCode)
                //@TODO 修改部门一级重名bug
                //.append(subsidiaryBizCodes)
                .append(directSubsidiaryBizCode)
                .append(active)
                .toHashCode();
    }

    @Override
    public String getKey() {
        return businessCode;
    }

    @Override
    public String getSubBizCodes() {
        if(subsidiaryBizCodes!=null&&subsidiaryBizCodes.size()>0) {
            return subsidiaryBizCodes.get(0);
        }
        return "";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean getUsable() {
        return true;
    }

}
