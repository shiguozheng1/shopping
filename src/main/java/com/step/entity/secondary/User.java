package com.step.entity.secondary;

import com.step.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by zhushubin  on 2019-12-18.
 * email:604580436@qq.com
 * Entity - 用户
 */
@Entity
@Table(name = "t_users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class User extends BaseEntity<Long> {
    private static final long serialVersionUID = -9164858807898583375L;

    /**
     * 密码找回类型
     */
    public enum PasswordType {

        /**
         * 会员
         */
        member,

        /**
         * 商家
         */
        business
    }

    /**
     * "登录失败尝试次数"缓存名称
     */
    public static final String FAILED_LOGIN_ATTEMPTS_CACHE_NAME = "failedLoginAttempts";

    /**
     * 是否启用
     */
    @NotNull
    @Column(nullable = false)
    private Boolean isEnabled;

    /**
     * 是否锁定
     */
    @Column(nullable = false)
    private Boolean isLocked;

    /**
     * 锁定日期
     */
    private Date lockDate;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 最后登录日期
     */
    private Date lastLoginDate;

}
