package com.step.entity.secondary;

import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.BaseEntity;
import com.step.utils.wrapper.Wrapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhushubin  on 2019-05-29.
 * email:604580436@qq.com
 * 权限管理-动作列表
 */
@Entity
@Table(name="t_element")
@Getter
@Setter
public class Element extends BaseEntity<Long> {
    public interface ElementView extends Wrapper.WrapperView{}
    public enum ElementType{
        button,uri
    }

    /***
     * 编码
     */
    @JsonView(ElementView.class)
    private String code;
    /***
     * 类型
     */
    @JsonView(ElementView.class)
    @Enumerated(EnumType.STRING)
    private ElementType type;
    /***
     * 名称
     */
    @JsonView(ElementView.class)
    private String name;
    /***
     * 链接地址
     */
    @JsonView(ElementView.class)
    private String uri;
    @JsonView(ElementView.class)
    private String path;
    @JsonView(ElementView.class)
    private String method;
    @JsonView(ElementView.class)
    private String description;
    /***
     * 菜单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    /**
     * 上级分类
     */
    @JsonView(ElementView.class)
    @ManyToOne(fetch = FetchType.LAZY)
    private Element parent;
    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Element> children = new HashSet<>();
    @Transient
    private Long menuId;

    /***
     * 按钮或资源与资源表一对多关系
     */
    @OneToMany(cascade=CascadeType.PERSIST,fetch=FetchType.LAZY,mappedBy="element")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private Set<ResourceAuthority> resourceAuthorities = new HashSet<>();
}
