package com.step.entity.dto;

import java.io.Serializable;

public abstract class DifferEntity implements Serializable {
    public abstract String getKey();


    public abstract String getSubBizCodes();//业务实体
    public abstract String getName();//修改name
    public abstract Boolean getUsable();//数据是否可用
    public String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
