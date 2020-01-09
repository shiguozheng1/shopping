package com.step.service.admin;

import com.step.entity.secondary.Role;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * shigz
 * 2019/11/8
 **/
public interface RoleService extends BaseService<Role,Long> {
    List<Role> findAll(Long id);

    /***
     * 根据id 获取角色
     * @param id
     * @return
     */
    Role find(Long id);

    /**
     * 根据code获取角色
     * @param code
     * @return
     */
    Role findByCode(String code);

    /**
     * 判断编号是否存在
     *
     * @param code
     *            编号(忽略大小写)
     * @return 编号是否存在
     */
    boolean codeExists(String code);

    /***
     * 新增菜单
     * @param entity
     * @return
     */
    Role create(Role entity);

    /***
     * 根绝spec 查找role
     * @param spc
     * @return
     */
    List<Role> findAll(Specification<Role> spc);

    /**
     * 修改
     * @param entity
     * @return
     */
    Role update(Role entity);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /***
     * 角色新增关联资源
     * @param roleId 角色id
     * @param menuIds 菜单ids
     * @param elementIds 按钮或资源id
     */
    void savePermissions(Long roleId, List<Long> menuIds, List<Long> elementIds);

    /**
     * 根据Id集合查询roles
     */
    List<Role> findAddByIds(List<Long> roleIds);
}
