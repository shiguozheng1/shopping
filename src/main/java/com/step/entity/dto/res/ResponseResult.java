package com.step.entity.dto.res;

import lombok.Data;

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 */
@Data
public class ResponseResult<T> {
    private Boolean success;
    private String message;
    private String code;
    private T data;
}
