package com.step.entity.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.BaseEntity;
import com.step.entity.OrderedEntity;
import com.step.entity.primary.Element;
import com.step.utils.wrapper.Wrapper;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author zhushubin
 * @date 2019-11-07
 * email:604580436@qq.com
 * 角色
 */
@Entity
@Table(name="t_role")
@Data
public class Role extends OrderedEntity<Long> {

    /**
     * 树路径分隔符
     */
    public interface BaseRoleView extends Wrapper.WrapperView{};
    public static final String TREE_PATH_SEPARATOR = ",";
    private static final long serialVersionUID = 7997285868038363904L;

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @JsonView(Role.BaseRoleView.class)
    private Long id;

    /***
     * 角色编码
     */
    @JsonView(BaseRoleView.class)
    private String code;
    /**
     * 名称
     */
    @NotBlank
    @Length(max = 200)
    @Column(nullable = false)
    @JsonView(BaseRoleView.class)
    private String name;

    /**
     * 是否内置
     */
    @Column(name="is_system",nullable = false, updatable = false)
    @JsonView(BaseRoleView.class)
    private Boolean isSystem = false;

    /**
     * 描述
     */
    @Length(max = 200)
    @JsonView(BaseRoleView.class)
    private String description;

    @JsonView(BaseRoleView.class)
    @Transient
    private Long parentId;

    @JsonView(BaseRoleView.class)
    @Transient
    @Column(name="roleType_id")
    private Long roleTypeId;
    /**
     * 层级
     */
    @Column(nullable = false)
    @JsonView(BaseRoleView.class)
    private Integer grade;

    /**
     * 管理员
     */
    @ManyToMany(cascade = CascadeType.DETACH,mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Admin> admins = new HashSet<>();


    /**
     * 树路径
     */
    @Column(name="tree_path",nullable = false)
    private String treePath;
    /**
     * 上级分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonView(BaseRoleView.class)
    private Role parent;
    /**
     * 下级分类
     */
    @OneToMany(cascade=CascadeType.PERSIST,mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("order desc")
    private Set<Role> children = new HashSet<>();
    /***
     * 角色与菜单一对多关系
     */
    @JsonView(BaseRoleView.class)
    @OneToMany(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY,mappedBy="role")
   private Set<ResourceAuthority> resourceAuthorities = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    private RoleType roleType;

    public Set<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Admin> admins) {
        this.admins = admins;
    }

    public Set<Role> getChildren() {
        return children;
    }

    public void setChildren(Set<Role> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){ return true;}

        if (o == null || getClass() != o.getClass()){ return false;
        }

        Role element = (Role) o;

        return new EqualsBuilder()
                .append(id, element.id)
                .append(name,element.name)
                .append(code, element.code)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(code)
                .toHashCode();
    }

    /**
     * 获取所有上级分类ID
     *
     * @return 所有上级分类ID
     */
    @Transient
    public Long[] getParentIds() {
        String[] parentIds = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
        Long[] result = new Long[parentIds.length];
        for (int i = 0; i < parentIds.length; i++) {
            result[i] = Long.valueOf(parentIds[i]);
        }
        return result;
    }

    public Long getParentId() {
        if(parentId!=null){
            return  parentId;
        }
        return getParent()==null?null:getParent().getId();
    }

    public Long getRoleTypeId() {
        if(roleTypeId!=null){
            return  roleTypeId;
        }
        return getRoleType()==null?null:getRoleType().getId();
    }

    @Override
    public String toString() {
        return "Role{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", isSystem=" + isSystem +
                ", description='" + description + '\'' +
                ", parentId=" + parentId +
                ", grade=" + grade +
                ", treePath='" + treePath + '\'' +
                '}';
    }
}
