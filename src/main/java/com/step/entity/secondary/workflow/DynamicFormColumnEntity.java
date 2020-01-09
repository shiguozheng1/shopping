package com.step.entity.secondary.workflow;

import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.primary.Element;
import com.step.utils.wrapper.Wrapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author zhushubin
 * @date 2019-11-21
 * email:604580436@qq.com
 */
@Entity
@Table(name="t_dynamic_form_column_entity")
@Getter
@Setter
public class DynamicFormColumnEntity implements Serializable{
    private static final long serialVersionUID = -5740823628430691224L;
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @JsonView(Wrapper.WrapperView.class)
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
    private Integer         sortIndex;
    //验证器配置
//    @ElementCollection
//    private List<String> validator;
    @ManyToOne
    @JoinColumn(name="form_id")
    private DynamicFormEntity formEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o){ return true;}

        if (o == null || getClass() != o.getClass()){ return false;
        }

        DynamicFormColumnEntity element = (DynamicFormColumnEntity) o;

        return new EqualsBuilder()
                .append(id, element.id)
                .append(name, element.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .toHashCode();
    }
}
