package com.step.entity.secondary.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

/**
 * Created by zhushubin  on 2019-11-25.
 * email:604580436@qq.com
 */
@Data
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class DynamicFormEntityVo {
    private Long id;
    //表单名称
    private String  name;
    //数据库表名
    private String  databaseTableName;
    //备注
    private String  describe;
    //版本
    private Long    version;
    //创建人id creatorId
    private String creater;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdDate;

    /**
     * 最后修改日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifiedDate;

    private Boolean deployed;
    //别名
    private String  alias;
    //触发器
    private String  triggers;
    //表链接
    private String  correlations;
    //表单类型
    private String  type;

    private String tags;

    private String sn;
}
