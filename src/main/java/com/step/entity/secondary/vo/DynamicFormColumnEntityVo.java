package com.step.entity.secondary.vo;

import lombok.Data;

/**
 * shigz
 * 2019/11/26
 **/
@Data
public class DynamicFormColumnEntityVo {
    protected Long id;
    //字段名称
    private String       name;
    //数据库列
    private String       columnName;
    //备注
    private String       describe;
    //别名
    private String       alias;
    //java类型
    private String       javaType;
    //jdbc类型
    private String       jdbcType;
    //数据类型
    private String       dataType;
    //长度
    private Integer      length;
    //精度
    private Integer      precision;
    //小数点位数
    private Integer      scale;
    //数据字典配置
    private String       dictConfig;
    //序号
    private Integer      sortIndex;
}
