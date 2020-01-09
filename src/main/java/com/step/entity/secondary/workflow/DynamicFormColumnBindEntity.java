package com.step.entity.secondary.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态表单和表单列关联实体
 *
 * @author zhouhao
 * @since 3.0
 */
public class DynamicFormColumnBindEntity implements Serializable{
    private DynamicFormEntity form;

    private List<DynamicFormColumnEntity> columns=new ArrayList<>();


    public DynamicFormColumnBindEntity() {
    }

    public DynamicFormColumnBindEntity(DynamicFormEntity form, List<DynamicFormColumnEntity> columns) {
        this.form = form;
        this.columns = columns;
    }

    public DynamicFormEntity getForm() {
        return form;
    }

    public void setForm(DynamicFormEntity form) {
        this.form = form;
    }

    public List<DynamicFormColumnEntity> getColumns() {
        return columns;
    }

    public void setColumns(List<DynamicFormColumnEntity> columns) {
        this.columns = columns;
    }
}
