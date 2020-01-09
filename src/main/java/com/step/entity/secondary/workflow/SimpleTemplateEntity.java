package com.step.entity.secondary.workflow;

import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.BaseEntity;
import com.step.utils.wrapper.Wrapper;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;

/**
 * shigz
 * 2019/11/22
 * 模板管理
 **/
@Entity
@Table(name="t_template_entity")
@Data
public class SimpleTemplateEntity extends BaseEntity<Long> implements Serializable {
    private static final long serialVersionUID = 4372072223155734143L;
    //模板标识
    private String code;
    //模板名称
    private String name;
    //模板类型
    private String type;
    //模板内容
    @Length(max = 2000)
    private String template;
    //模板配置
    private String config;
    //版本号
    private Long version;
    //模板分类
    private String classified;
}
