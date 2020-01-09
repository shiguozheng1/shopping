package com.step.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * Controller - 基类
 */
public abstract class BaseController {
    @Autowired
    protected Validator validator;

    /**
     * 数据验证
     *
     * @param target
     *            验证对象
     * @param groups
     *            验证组
     * @return 验证结果
     */
    protected List<String> validater(Object target, Class<?>... groups) {
        Assert.notNull(target);
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target, groups);
        if (constraintViolations.isEmpty()) {
            return null;
        }
     return   constraintViolations.stream().map(e->{
            return e.getMessage();
        }).collect(Collectors.toList());
    }
}
