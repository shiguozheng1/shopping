package com.step.entity.secondary.form;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhushubin  on 2019-11-13.
 * email:604580436@qq.com
 * 给角色分配权限表单
 */
@Data
public class PermissionForm implements Serializable {
    private static final long serialVersionUID = 6003344635143805993L;
    private Long roleId;//角色
    private List<Long> menuIds;//菜单
    private List<Long> elementIds;//按钮或资源
}
