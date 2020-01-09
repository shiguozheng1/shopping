package com.step.service.admin;


import com.step.entity.secondary.Menu;

import java.util.List;

/**
 * Created by zhushubin  on 2019-06-03.
 * email:604580436@qq.com
 */
public interface MenuService extends BaseService<Menu,Long>{
    /**
     * 判断编号是否存在
     *
     * @param code
     *            编号(忽略大小写)
     * @return 编号是否存在
     */
    boolean codeExists(String code);
    public List<Menu> findAll();

    /***
     * 获取username 有权限访问的菜单页
     * @param username
     * @return
     */
    List<Menu> findMenusByUsername(String username);

    /***
     * 根据组id 获取菜单
     * @param id 组id
     * @return list
     */
    List<Menu> findMenuByAuthorityId(Long id, String groupType);

    /***
     * 新增菜单
     * @param entity
     * @return
     */
    Menu create(Menu entity);
    /***
     * 根据id 获取菜单
     * @param parentId
     * @return
     */
    Menu find(Long parentId);
    /***
     * 修改菜单
     * @param entity
     * @return
     */
    Menu update(Menu entity);

    /***
     * 根据code 查找 menu
     * @param code
     * @return
     */
    Menu findByCode(String code);

    /***
     * 删除菜单
     * @param id
     */
    void delete(Long id);
}
