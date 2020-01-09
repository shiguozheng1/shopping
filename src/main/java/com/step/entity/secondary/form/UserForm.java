package com.step.entity.secondary.form;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * shigz
 * 2019/11/14
 **/
@Data
public class UserForm implements Serializable {

    private static final long serialVersionUID = -5673231181525653434L;

    private String username;
    private Long id;
    private String avatar;
    private List<Long> roles;
    private Integer sex;
    private String password;

}
