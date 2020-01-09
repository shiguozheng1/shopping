package com.step.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.step.entity.dto.res.employee.EmpDepInfoDto;
import com.step.utils.StringCommonUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * shigz
 * 2019/9/4
 **/
@Data
@JsonIgnoreProperties({"successors","method","modifyName"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaycurEmployee extends DifferEntity{
    private static final String  EMAIL_TAIL = "@default.com";
    private  String mobile="";//	手机号码
    private  String email="";//	邮箱
    private  String name="";//员工姓名
    private  String employeeId="";	//员工工号
    private  String rank="";//员工职级名称
    private String position="";//员工职级编码
    private String status="ENABLE";//员工在职状态, 在职（ENABLE） 离职（DISABLE）
    private String defaultSubsidiaryBizCode;//员工默认业务实体
    private Long hireDate;//员工入职时间
    private String tag;	//属性
    private String residenceCode;//常驻地，传入每刻地址编码，具体见备注
    private String custField1;//自定义字段1
    private String custField2;//自定义字段2
    private String custField3;//自定义字段3
    private String custField4;//自定义字段4
    private String custField5;//自定义字段5
    private String custField6;//自定义字段6
    private String custField7;//自定义字段7
    private String custField8;//自定义字段8
    private String custField9;//自定义字段9
    private String custField10;//自定义字段10
    private String note;//备注
    private String firstName;//名
    private String middleName;//多为非中国人填写使用
    private String lastName;//姓
    private List<EmpDepInfoDto> departments;
    private String source;//外联平台
    private String sourceId;//外联平台key 格式:外联平台的企业ID+"_"+外联平台的用户ID
    private List<String> restrictSubsidiaryBizCodes;//限定的业务实体
    private String successors;
    private Boolean notifyActivation;//是否发送加入通知
    private String method;
    private Object modifyName;
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {return false;}

        MaycurEmployee maycurEmployee = (MaycurEmployee) o;

        return new EqualsBuilder()
                .append(getMobile(), maycurEmployee.getMobile())
                .append(name, maycurEmployee.name)
                .append(getEmail(),maycurEmployee.getEmail())
                .append(employeeId,maycurEmployee.employeeId)
                .append(departments,maycurEmployee.departments)
                .append(defaultSubsidiaryBizCode,maycurEmployee.defaultSubsidiaryBizCode)
                .append(status,maycurEmployee.status)
                .append(rank,maycurEmployee.rank)
                .append(getPosition(),maycurEmployee.getPosition())
                .append(custField1,maycurEmployee.custField1)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getMobile())
                .append(name)
                .append(getEmail())
                .append(employeeId)
                .append(getPosition())
                .append(departments)
                .append(defaultSubsidiaryBizCode)
                .append(status)
                .append(rank)
                .append(custField1)
                .toHashCode();
    }

    @Override
    public String getKey() {
        return employeeId;
    }

    @Override
    public String getSubBizCodes() {
        return defaultSubsidiaryBizCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean getUsable() {
        return status.equals("ENABLE");
    }

    public String getMobile() {
        if(StringUtils.isEmpty(mobile)){
            return "";
        }
        return mobile.trim();
    }

    public String getEmail() {
        if(StringUtils.isNotEmpty(email)){
            return email.trim();
        }
        return "";
//        email = getEmployeeId()+EMAIL_TAIL;
//        return email;
    }

    public String getPosition() {
        if(StringUtils.isEmpty(position)){
            return "0";
        }
        return position;
    }
}
