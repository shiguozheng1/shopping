package com.step.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by zhushubin  on 2019-08-28.
 * email:604580436@qq.com、
 * 判断是否启用域控管理
 */
public class LdapCondition  implements Condition {
        @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        boolean enable =  environment.getProperty("ldap.enable",Boolean.class);
        return enable;
    }
}
