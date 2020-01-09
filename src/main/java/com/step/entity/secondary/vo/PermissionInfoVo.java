package com.step.entity.secondary.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author zhushubin
 * @date 2019-11-14
 * email:604580436@qq.com
 * 资源授权信息
 */
@Data
public class PermissionInfoVo implements Serializable{
    private static final long serialVersionUID = -7181967905501368594L;
    private Long id;
    private String code;
    private String type;
    private String uri;
    private String method;
    private String name;
    private String menu;
}
