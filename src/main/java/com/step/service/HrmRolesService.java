package com.step.service;

import com.step.entity.primary.HrmRoles;

import java.util.List;

/**
 * Created by zhushubin  on 2019-09-16.
 * email:604580436@qq.com
 * 角色
 */
public interface HrmRolesService {
    /***
     * 根据名称匹配
     * @param name
     * @return
     */
    List<HrmRoles> findByRolesNameLikeOrderByRolesNameAsc(String name);

    /***
     * 根据角色找到用户id
     * @param id 用户id
     * @return null
     */
    List<String> findMemberByRoleId(Long id);
}
