package com.step.entity.secondary.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * shigz
 * 2019/11/26
 **/
@Data
public class DynamicFormColumnBindEntityVo {
    private DynamicFormEntityVo form;

    private List<DynamicFormColumnEntityVo> columns=new ArrayList<>();


    public DynamicFormColumnBindEntityVo() {
    }

    public DynamicFormColumnBindEntityVo(DynamicFormEntityVo form, List<DynamicFormColumnEntityVo> columns) {
        this.form = form;
        this.columns = columns;
    }
}
