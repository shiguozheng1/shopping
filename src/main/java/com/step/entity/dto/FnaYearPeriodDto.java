package com.step.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhushubin  on 2019-08-30.
 * email:604580436@qq.com
 * 账期
 */
@Data
public class FnaYearPeriodDto {
    /***
     * 会计期间
     */
    private Integer  Periodsid;
    private String startdate;
    private String enddate;
}
