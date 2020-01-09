package com.step.entity.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author user
 * @author user
 * @date 2019-04-17
 */
@Entity
@Table(name = "HrmSubCompany")
@Setter
public class HrmSubCompany implements Serializable{
    /**
     * 树路径分隔符
     */
//    public static final String TREE_PATH_SEPARATOR = "o=";
    public static final String TREE_PATH_SEPARATOR = "ou=";
    @Id
    private Long id;
    @Column(name="subcompanyname")
    private String name;
    @Column(name="showorder")
    private Integer showOrder;
    @Column(name="canceled")
    private Boolean canceled=false;
    /**
     * 上级分类
     */
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false,fetch = FetchType.EAGER)
    @JoinColumn(name="supsubcomid")
    @NotFound(action= NotFoundAction.IGNORE)
    private HrmSubCompany parent;

    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent",cascade={CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties
    private Set<HrmSubCompany> children = new HashSet<>();

    /**
     * 下级部门
     */
    @OneToMany(mappedBy = "hrmSubCompany",cascade={CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties
    private Set<HrmDepartment> departments = new HashSet<>();
    /**
     * 下级人员
     */
    @OneToMany(mappedBy = "hrmSubCompany",cascade={CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<HrmResource> resources = new HashSet<>();

    @Transient
    public Boolean getCpCanceled(){
        if(this.canceled==null){
            return canceled=false;
        }
        return this.canceled;
    }

    @Transient
    public String getTreePath(){
        StringBuilder builder = new StringBuilder();
        HrmSubCompany hrmSubCompany= null;
        try {
            hrmSubCompany = this.getParent()==null?null:(HrmSubCompany)this.getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(hrmSubCompany!=null){
           return builder.append(HrmDepartment.TREE_PATH_SEPARATOR + getName() +",").append(hrmSubCompany.getTreePath()).toString();
        }else{
            return HrmSubCompany.TREE_PATH_SEPARATOR + getName() +",";
        }
    }
    @Transient
    public Long getCpId(){
        return getId()+1000;
    }

    @Transient
    public Long getCpParentedId(){
        if(getParent()==null){
            return 1L;
        }
        return getParent().getId()+1000;
    }

    public Integer getShowOrder() {
        if(showOrder==null){
            return 0;
        }
        return 10000-showOrder;
    }

    @Override
    public String toString() {
        return "HrmSubCompany{" +
                "name='" +'\'' +
                '}';
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }





    public HrmSubCompany getParent() {
        return parent;
    }


    public Set<HrmSubCompany> getChildren() {
        return children;
    }


    public Set<HrmDepartment> getDepartments() {
        return departments;
    }


    public Set<HrmResource> getResources() {
        return resources;
    }

    public Boolean getCanceled() {
        return canceled;
    }
}
