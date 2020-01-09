package com.step.entity.dto.res;

import com.google.common.collect.Lists;
import com.step.entity.dto.MaycurCompany;
import lombok.Data;

import java.util.List;

/**
 * shigz
 * 2019/8/29
 **/
@Data
public class ResponsePostData<T>  extends BaseResponse{
    private T data;
    private Object errorData;
}
