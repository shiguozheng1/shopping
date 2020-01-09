package com.step.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @Author: shiguozheng
 * @Date: 2019/8/28
 **/
@Data
@JsonIgnoreProperties({"method","modifyName"})
public class MaycurCompany extends DifferEntity  {
    private static final long serialVersionUID = -4757330004624397234L;
    private String name ;//业务实体名称，公司内唯一
    private String businessCode;//业务实体编码,若businessCode已存在，则更新已有数据
    private String baseCurrency="CNY";//业务实体本币
    private String principal="";//业务实体负责人工号
    private Boolean active;//是否启用
    private String parentBizCode="";//上级业务实体编码
    private String method;//update 或者 create
    private String modifyName;
    public boolean equals(Object o) {
        if (this == o){ return true;}

        if (o == null || getClass() != o.getClass()){ return false;}

        MaycurCompany maycurCompany = (MaycurCompany) o;

        return new EqualsBuilder()
                .append(businessCode, maycurCompany.businessCode)
                .append(name, maycurCompany.name)
                .append(parentBizCode,maycurCompany.parentBizCode)
                .append(principal,maycurCompany.principal)
                .append(active,maycurCompany.active)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(businessCode)
                .append(name)
                .append(parentBizCode)
                .append(principal)
                .append(active)
                .toHashCode();
    }

    @Override
    public String getKey() {
        return businessCode;
    }

    @Override
    public String getSubBizCodes() {
        return businessCode;
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
