package com.step.entity.dto.res.employee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * shigz
 * 2019/9/4
 **/
@Data
@JsonIgnoreProperties(value = {"employeeId"})
public class EmpDepInfoDto {
    private String departmentBizCode;
    private String managerId="";
    private String employeeId="";
    private String cover="Y";
    /***
     * 直属
     */
    private String positionBizCode;
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmpDepInfoDto empDepInfoDto = (EmpDepInfoDto) o;

        return new EqualsBuilder()
                .append(departmentBizCode, empDepInfoDto.departmentBizCode)
                .append(managerId, empDepInfoDto.managerId)
                .append(cover,empDepInfoDto.cover)
                .append(employeeId,empDepInfoDto.employeeId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(departmentBizCode)
                .append(managerId)
                .append(employeeId)
                .append(cover)
                .toHashCode();
    }
}
