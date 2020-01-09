package com.step.entity.secondary;

import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.BaseEntity;
import com.step.entity.OrderedEntity;
import com.step.utils.wrapper.Wrapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author zhushubin
 * @date 2019-11-08
 * email:604580436@qq.com
 * 资源授权
 */
@Entity
@Table(name="t_resource_authority")
@Getter
@Setter
public class ResourceAuthority extends OrderedEntity<Long> {
    private static final long serialVersionUID = -9212878114622027772L;
    public final static String RESOURCE_ACTION_VISIT = "访问";

    /***
     * 资源类型
     */
    public enum ResourceType{
        button,menu
    };
    public enum AuthorityType{
        group
    }
    /***
     * 授权id 对应 authorityType (组，人员)所指定的对象
     * 如果authorityType 为role 则为角色
     * 如果authorityType 为member则为人员
     */
//    @Column(name = "authority_id")
//    private Long authorityId;

    @Column(name = "authority_type")
    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;

    /***
     * 资源类型
     */
    @Column(name = "resource_type")
    @Enumerated(EnumType.STRING)
    @JsonView(Wrapper.WrapperView.class)
    private ResourceType resourceType;
    /***
     * 多对一，多个资源授权对一一个角色
     */
    @ManyToOne(cascade= CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name="authority_id")
    private Role role;
    /***
     * 多对一，多个资源授权对一一个角色
     * nullable=false,
     */
    @ManyToOne(cascade= CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name="menu_id",foreignKey =@ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    @JsonView(Wrapper.WrapperView.class)
    private Menu menu;
    /***
     * 多对一，多个资源授权对一一个角色
     */
    @ManyToOne(cascade= CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinColumn(name="element_id",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    @JsonView(Wrapper.WrapperView.class)
    private Element element;


    /**
     * 上级分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private ResourceAuthority parent;
    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("order asc")
    private Set<ResourceAuthority> children = new HashSet<>();

    /**
     * 树路径
     */
    @Column(name="tree_path")
    private String treePath;
    /**
     * 获取所有上级分类
     *
     * @return 所有上级分类
     */
    @Transient
    public List<ResourceAuthority> getParents() {
        List<ResourceAuthority> parents = new ArrayList<>();
        recursiveParents(parents, this);
        return parents;
    }


    /**
     * 递归上级分类
     *
     * @param parents
     *            上级分类
     * @param menu
     *            商品分类
     */
    private void recursiveParents(List<ResourceAuthority> parents, ResourceAuthority menu) {
        if (menu == null) {
            return;
        }
        ResourceAuthority parent = menu.getParent();
        if (parent != null) {
            parents.add(0, parent);
            recursiveParents(parents, parent);
        }
    }
    @Transient
    public static ResourceAuthority buildMenu() {
        ResourceAuthority resourceAuthority = new ResourceAuthority();
        resourceAuthority.setAuthorityType(AuthorityType.group);
        resourceAuthority.setResourceType(ResourceType.menu);
        resourceAuthority.setTreePath(",");
        return resourceAuthority;
    }
    @Transient
    public static ResourceAuthority buildButton() {
        ResourceAuthority resourceAuthority = new ResourceAuthority();
        resourceAuthority.setAuthorityType(AuthorityType.group);
        resourceAuthority.setResourceType(ResourceType.button);
        resourceAuthority.setTreePath(",");
        return resourceAuthority;
    }

}
