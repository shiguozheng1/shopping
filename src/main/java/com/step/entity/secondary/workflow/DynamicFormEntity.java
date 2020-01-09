package com.step.entity.secondary.workflow;


import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.BaseEntity;
import com.step.entity.secondary.Admin;
import com.step.utils.wrapper.Wrapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author zhushubin
 * @date 2019-11-21
 * email:604580436@qq.com
 */
@Entity
@Table(name="t_dynamic_form_entity")
@Getter
@Setter
public class DynamicFormEntity extends BaseEntity<Long> {
    public interface DynamicFormEntityView extends Wrapper.WrapperView{}
    private static final long serialVersionUID = 3458907782838717866L;
    /***
     * 表单编号
     */
    @Column(name="sn")
    private String sn;
    //表单名称
    @JsonView(DynamicFormEntityView.class)
    private String  name;
    //数据库表名
    @Column(name="t_name")
    @JsonView(DynamicFormEntityView.class)
    private String  databaseTableName;
    //备注
    private String  describe;
    //版本
    private Long    version;
    //创建人id creatorId
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false,fetch = FetchType.EAGER)
    @JoinColumn(name="creator_id")
    @NotFound(action= NotFoundAction.IGNORE)
    @JsonView(DynamicFormEntityView.class)
    private Admin creater;

    //是否已发布
    @Column(name="is_deployed")
    @JsonView(DynamicFormEntityView.class)
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
    @OneToMany(mappedBy = "formEntity",cascade ={CascadeType.PERSIST,CascadeType.REMOVE} )
    private Set<DynamicFormColumnEntity> colums = new HashSet<>();
    @Override
    public boolean equals(Object o) {
        if (this == o){ return true;}

        if (o == null || getClass() != o.getClass()){ return false;
        }

        DynamicFormEntity element = (DynamicFormEntity) o;

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
