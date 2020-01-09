package com.step.entity.primary.vo;

import lombok.Data;

/**
 * Created by zhushubin  on 2019-09-24.
 * email:604580436@qq.com
 */
@Data
public class CostTrackingInfo {
    /***
     * 选项属于的辅助核算名称
     */
    private String costTrackingName ="项目";
    /***
     * 辅助核算选项名称
     */
    private String name;
    /***
     * 辅助核算编码
     */
    private String businessCode;
    /***
     * 默认值及历史数据为
     *不可授权；可选值：NONE,
     *RESP_USER,RESP_AND_AUTH_USER；
     *RESP_USER表示负责人可授权，
     *RESP_AND_AUTH_USER表示
     *负责人与可见人可授权，
     *NONE：不可授权
     */
    private String visibilityAuthLevel = "NONE";
    private Boolean  active=true;
    private String visibility = "WHITE_LIST";
    /***
     * 项目负责人
     */
    private String principal;
}
