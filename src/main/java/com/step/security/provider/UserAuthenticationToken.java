package com.step.security.provider;

import com.step.entity.secondary.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Created by zhushubin  on 2019-12-18.
 * email:604580436@qq.com
 * Security - 用户认证令牌
 */
@Getter
@Setter
public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private static final long serialVersionUID = -3317011036990045047L;
    /**
     * 用户类型
     */
    private Class<? extends User> userClass;
    public UserAuthenticationToken(Class<? extends User> userClass,Object principal, Object credentials) {
        super(principal, credentials);
        this.userClass = userClass;
    }
}
