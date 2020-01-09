package com.step.entity.dto.res;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhushubin  on 2019-09-17.
 * email:604580436@qq.com
 */
@Data
public class UserGroupResponse implements Serializable {
    public String    groupName;
    public String    groupBusinessCode;
    public String    subGroupName;
    public String    message;
    public Boolean   success;
}