package com.step.service.admin;

import com.step.entity.secondary.ResourceAuthority;

import java.util.List; /**
 * Created by zhushubin  on 2019-11-13.
 * email:604580436@qq.com
 */
public interface PermissionService {
    /***
     * 给角色新增授权资源
     * @param roleId 角色id
     * @param menuIds 菜单id
     * @param elementIds 按钮或资源id
     */
    void save(Long roleId, List<Long> menuIds, List<Long> elementIds);

    /***
     * 根据角色ids 获取授权资源
     * @param roleIds 角色ids
     * @return list
     */
    List<ResourceAuthority> findByRoleIds(List<Long> roleIds);
}
