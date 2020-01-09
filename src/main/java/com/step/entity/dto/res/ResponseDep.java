package com.step.entity.dto.res;

import lombok.Data;

import java.util.List;

/**
 * 部门查询的响应实体
 * shigz
 * 2019/9/2
 **/
@Data
public class ResponseDep {
    private String name;
    private String businessCode;
    private String costCenterCode;
    private List<String> subsidiaryBizCodes;
    private Boolean enabled;
    private String principleId;
    private String parentBizCode;
    private String directSubsidiaryBizCode;
}
