package com.step.entity.secondary.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.secondary.Admin;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author zhushubin
 * @date 2019-11-08
 * email:604580436@qq.com
 * 管理员信息
 */
@Data
public class AdminVo implements Serializable{
    private static final long serialVersionUID = -4310533949120646060L;
    private Long id;
    /***
     * 用戶名
     */
    private String username;
    /***
     * 头像
     */
    private String avatar;

    /**
     * 是否启用
     */
    private Boolean isEnabled;

    /**
     * 是否锁定
     */
    private Boolean isLocked;

    /**
     * 创建日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdDate;

    @Column(name="sex")
    private Integer sex;

    /**
     * 锁定日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date lockDate;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 最后登录日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date lastLoginDate;
    private List<PermissionInfoVo> menus ;
    private List<PermissionInfoVo> elements ;
}
