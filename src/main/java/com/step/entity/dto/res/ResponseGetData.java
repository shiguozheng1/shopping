package com.step.entity.dto.res;

import lombok.Data;

import java.util.List;

/**
 * shigz
 * 2019/9/4
 **/
@Data
public class ResponseGetData<T> {
    private String code;
    private String message;
    private List<T> data;
    private String args;
    private Boolean linkDetail;
    private Boolean last;
    private Boolean nonBizError;
    private Object errorData;
}
