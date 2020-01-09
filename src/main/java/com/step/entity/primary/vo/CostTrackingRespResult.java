package com.step.entity.primary.vo;

import lombok.Data;

/**
 * shigz
 * 2019/9/24
 **/
@Data
public class CostTrackingRespResult {
    private String costTrackingName;
    private String costTrackingItemBizCode;
    private String costTrackingItemName;
    private String createdAt;
    private String updatedAt;
    private Boolean active;
    private String subsidiaryCodes;
    private String visibleDepartmentCodes;
    private String visibleEmployeeIds;
}
