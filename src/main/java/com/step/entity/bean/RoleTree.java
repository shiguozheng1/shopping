package com.step.entity.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * shigz
 * 2019/11/8
 **/
@Data
public class RoleTree extends TreeNode implements Serializable {
    private static final long serialVersionUID = 1917396040179057692L;
    private String description;
    private String name;
    private Long roleTypeId;
}
