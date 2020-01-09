package com.step.entity.primary;

import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

/**
 * Created by user on 2019-04-18.
 */
@Entity
@Table(name = "HrmResource")
@Setter
public class HrmResource {
    public static final String TREE_PATH_SEPARATOR = "cn=";
    @Id
    private Long id;
    @Column(name="loginid")
    private String loginid;
    @Column(name="lastname")
    private String lastname;
    @Column(name="mobile")
    private String mobile;
    @Column(name="email")
    private String email;
    @Column(name="status")
    private Integer status;
    @Column(name="sex")
    private Integer sex;
    @Column(name="workcode")
    private String workcode;
    @Column(name = "seclevel")
    private Long seclevel;



    /**
     * 上级人员
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "managerid")
    @NotFound(action = NotFoundAction.IGNORE)
    private HrmResource manager;

    /**
     * 上级部门
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentid")
    @NotFound(action = NotFoundAction.IGNORE)
    private HrmDepartment hrmDepartment;
    /**
     * 上级分类
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "subcompanyid1")
    @NotFound(action = NotFoundAction.IGNORE)
    private HrmSubCompany hrmSubCompany;



    @Transient
    public String getTreePath(){
        StringBuilder builder = new StringBuilder();
        return builder.append(HrmResource.TREE_PATH_SEPARATOR+this.getLoginid()+",")
            .append(this.getHrmDepartment().getTreePath())
            .toString();
}

    public Long getId() {
        return id;
    }

    public String getLoginid() {
        return loginid;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public String getWorkcode(){
        return workcode;
    }

    public String getEmail() {
        return email;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getSex() {
        return sex;
    }

    public HrmDepartment getHrmDepartment() {
        return hrmDepartment;
    }

    public HrmSubCompany getHrmSubCompany() {
        return hrmSubCompany;
    }

    public HrmResource getManager() {
        return manager;
    }

    public void setManager(HrmResource manager) {
        this.manager = manager;
    }

    public Long getSeclevel() {
        return seclevel;
    }
}
