package com.step.entity.secondary.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zhushubin  on 2019-11-29.
 * email:604580436@qq.com
 */
@Getter
@Setter
public class ProcessDefinitionVo implements Serializable{
    private String id;
    protected String name;
    protected String description;
    protected String key;
    protected int version;
    protected String category;
}
