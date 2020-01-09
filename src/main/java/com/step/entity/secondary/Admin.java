package com.step.entity.secondary;

import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.BaseEntity;
import com.step.entity.secondary.workflow.DynamicFormEntity;
import com.step.utils.wrapper.Wrapper;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * shigz
 * 2019/10/25
 * 登录人员信息
 **/
@Entity
@Table(name="t_admin")
@Data
public class Admin  extends User {
    private static final long serialVersionUID = 2298362620341006240L;

    public interface AdminView extends Wrapper.WrapperView {
    }

    /**
     * 用户名
     */
    @JsonView(AdminView.class)
    @Length( max = 20)
    @Pattern.List({@Pattern(regexp = "^[0-9a-zA-Z_\\u4e00-\\u9fa5]+$"), @Pattern(regexp = "^.*[^\\d].*$")})
    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    @Column(name="sex")
    private Integer sex;



    /**
     * 加密密码
     */
    @Column(name = "encoded_password")
    private String encodedPassword ="123456";
    /***
     * 头像
     */
    @JsonView(AdminView.class)
    @Column(name = "avatar")
    private String avatar;



    /**
     * 角色
     */
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    @OneToMany(mappedBy = "creater")
    private Set<DynamicFormEntity> dynamicFormEntities = new HashSet<>();

    public Set<DynamicFormEntity> getDynamicFormEntities() {
        return dynamicFormEntities;
    }

    public void setDynamicFormEntities(Set<DynamicFormEntity> dynamicFormEntities) {
        this.dynamicFormEntities = dynamicFormEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){ return true;}

        if (o == null || getClass() != o.getClass()){ return false;
        }

        Admin element = (Admin) o;

        return new EqualsBuilder()
                .append(id, element.id)
                .append(username,element.username)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(username)
                .toHashCode();
    }
}
