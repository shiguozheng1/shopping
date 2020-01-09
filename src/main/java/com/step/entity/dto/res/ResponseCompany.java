package com.step.entity.dto.res;

import lombok.Data;

/**
 * shigz
 * 2019/9/3
 **/
@Data
public class ResponseCompany {
    private Boolean enabled;
    private String name;
    private String businessCode;
    private String principle;
    private String baseCcy;
    private String parentBizCode;
}
