package com.step.entity.dto.res;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhushubin  on 2019-09-17.
 * email:604580436@qq.com
 */
@Data
public  class BaseResponse implements Serializable{
    protected String code;
    protected String message;
    protected String args;
    protected Boolean linkDetail;
    protected Boolean nonBizError;
}
