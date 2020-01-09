package com.step.entity.primary;

import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 2019-04-18.
 */
@Entity
@Table(name = "HrmDepartment")
@Setter
public class HrmDepartment implements Serializable {
    /**
     * 树路径分隔符
     */
    public static final String TREE_PATH_SEPARATOR = "ou=";
    @Id
    private Long id;
    @Column(name = "departmentname")
    private String name;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "subcompanyid1", referencedColumnName = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private HrmSubCompany hrmSubCompany;
    /**
     * 上级分类
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "supdepid")
    @NotFound(action = NotFoundAction.IGNORE)
    private HrmDepartment parent;


    /**
     * 下级分类
     */
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<HrmDepartment> children = new HashSet<>();



    /**
     * 下级人员
     */
    @OneToMany(mappedBy = "hrmDepartment",cascade={CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<HrmResource> resources = new HashSet<>();


    @Column(name = "canceled")
    private Boolean canceled = false;
    @Column(name = "showorder")
    private Integer showOrder;


    @Override
    public String toString() {
        return "HrmDepartment{" +
                "name='" + name + '\'' +
                '}';
    }

    public Integer getShowOrder() {
        if(showOrder==null){
            return 0;
        }
        return 10000-showOrder;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public HrmSubCompany getHrmSubCompany() {
        return hrmSubCompany;
    }

    public HrmDepartment getParent() {
        return parent;
    }

    public Set<HrmDepartment> getChildren() {
        return children;
    }

    public Set<HrmResource> getResources() {
        return resources;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    @Transient
    public String getTreePath(){
        StringBuilder builder = new StringBuilder();
        HrmDepartment hrmDepartment= null;
        try {
            hrmDepartment = this.getParent()==null?null:(HrmDepartment)this.getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(hrmDepartment!=null){
            return builder.append(HrmDepartment.TREE_PATH_SEPARATOR + getName() +",").append(hrmDepartment.getTreePath()).toString();
        }else{
            return HrmDepartment.TREE_PATH_SEPARATOR + getName() +","  + this.getHrmSubCompany().getTreePath();
        }


    }

    @Transient
    public Boolean getCpCanceled(){
        if(this.canceled==null){
            return canceled=false;
        }
        return this.canceled;
    }



}
