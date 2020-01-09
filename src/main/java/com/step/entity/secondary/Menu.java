package com.step.entity.secondary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.OrderedEntity;
import com.step.utils.wrapper.Wrapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhushubin  on 2019-05-29.
 * email:604580436@qq.com
 */
@Entity
@Table(name="t_menu")
@Getter
@Setter
public class Menu extends OrderedEntity<Long> {
    public interface BaseMenuView extends Wrapper.WrapperView{};
    private static final long serialVersionUID = -1943693226715369717L;
    /**
     * 树路径分隔符
     */
    public static final String TREE_PATH_SEPARATOR = ",";
    public enum MenuType{
        MENU,DIRT
    }
    /***
     * 编码
     */
    @JsonView(BaseMenuView.class)
    private String code;
    /***
     * 标题
     */
    @JsonView(BaseMenuView.class)
    private String title;
    /**
     * 全称
     */
    @Column(name="full_name",nullable = false, length = 500)
    private String fullName;
    /***
     * 访问路劲
     */
    @JsonView(BaseMenuView.class)
    private String href;
    /***
     * 图标
     */
    @JsonView(BaseMenuView.class)
    private String icon;
    /***
     * 菜单类型
     */
    @JsonView(BaseMenuView.class)
    @Enumerated(EnumType.STRING)
    private MenuType type;
    /***
     * 描述信息
     */
    @JsonView(BaseMenuView.class)
    private String description;
    /***
     * 路径
     */
    @JsonView(BaseMenuView.class)
    private String path;
    /***
     * 是否可用
     */
    @JsonView(BaseMenuView.class)
    @Column(name="enabled")
    private boolean enabled;
    @JsonView(BaseMenuView.class)
    @Transient
    private Long parentId;
    /**
     * 一个菜单对应多个资源或按钮
     * 一对多动作
     */
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private Set<Element> elements = new HashSet<Element>();

    /**
     * 树路径
     */
    @Column(name="tree_path",nullable = false)
    private String treePath;
    /**
     * 层级
     */
    @Column(nullable = false)
    @JsonView(BaseMenuView.class)
    private Integer grade;
    /**
     * 上级分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Menu parent;
    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("order asc")
    @JsonIgnore
    private Set<Menu> children = new HashSet<>();


    /***
     * 菜单资源一对多关系
     */
    @OneToMany(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY,mappedBy="menu")
    private Set<ResourceAuthority> resourceAuthorities = new HashSet<>();
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

    /**
     * 获取所有上级分类
     *
     * @return 所有上级分类
     */
    @Transient
    public List<Menu> getParents() {
        List<Menu> parents = new ArrayList<>();
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
    private void recursiveParents(List<Menu> parents, Menu menu) {
        if (menu == null) {
            return;
        }
        Menu parent = menu.getParent();
        if (parent != null) {
            parents.add(0, parent);
            recursiveParents(parents, parent);
        }
    }

    public Long getParentId() {
        if(parentId!=null){
            return  parentId;
        }
       return getParent()==null?null:getParent().getId();
    }
}
