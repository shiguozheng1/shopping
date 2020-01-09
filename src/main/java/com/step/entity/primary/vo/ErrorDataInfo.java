package com.step.entity.primary.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * shigz
 * 2019/11/18
 *
 * @author user*/
@Data
public class ErrorDataInfo implements Serializable {
    private static final long serialVersionUID = 795142724274469248L;
    private String id;
    private String message;
    private String loginId;
    private String lastname;
}
