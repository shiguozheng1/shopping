package com.step.action.admin;

import com.step.entity.secondary.form.PermissionForm;
import com.step.service.admin.PermissionService;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhushubin  on 2019-11-13.
 * email:604580436@qq.com
 *授权资源管理
 */
@RestController("adminPermissionController")
@RequestMapping("/admin/permission")
@Slf4j
public class PermissionController {
    @Autowired
   private PermissionService permissionService;
    /***
     * 给角色分配权限
     * @return
     */
    @PostMapping
    public Wrapper addPermissions(@RequestBody PermissionForm permissionForm){
        Assert.notNull(permissionForm.getRoleId(),"roleId is required");
        try {
            permissionService.save(permissionForm.getRoleId(), permissionForm.getMenuIds(), permissionForm.getElementIds());
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            WrapMapper.error("操作失败");
        }

        return WrapMapper.ok();
    }
}
