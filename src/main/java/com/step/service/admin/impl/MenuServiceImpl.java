package com.step.service.admin.impl;

import com.step.dao.MenuDao;
import com.step.entity.secondary.Menu;
import com.step.repository.secondary.MenuRepository;
import com.step.service.admin.BaseService;
import com.step.service.admin.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author zhushubin
 * @date 2019-11-04
 * email:604580436@qq.com
 * 菜单service
 */
@Service
@Transactional("transactionManagerSecondary")
@Slf4j
public class MenuServiceImpl extends BaseServiceImpl<Menu,Long> implements MenuService{
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuDao menuDao;
    @Override
    public boolean codeExists(String code) {
      return  menuDao.exists("code", code, true);
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll(new Sort(Sort.Direction.ASC,"order"));
    }

    @Override
    public List<Menu> findMenusByUsername(String username) {
        return null;
    }

    @Override
    public List<Menu> findMenuByAuthorityId(Long id, String groupType) {
        return null;
    }

    @Override
    public Menu create(Menu entity) {
        Assert.notNull(entity);
        setValue(entity);
        return menuRepository.save(entity);
    }

    @Override
    public Menu find(Long parentId) {
        Optional<Menu> menu =  menuRepository.findById(parentId);
        if(menu.isPresent()){
            return menu.get();
        }
        return null;
    }

    /***
     * 修改
     * @param entity
     * @return
     */
    @Override
    public Menu update(Menu entity) {
        Assert.notNull(entity);
        setValue(entity);
        return menuDao.merge(entity);
    }

    @Override
    public Menu findByCode(String code) {
        Menu menu= menuRepository.findByCode(code);
        return menu;
    }

    @Override
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    /***
     * 保存数据前计算父节点信息
     * @param entity
     */
    private void setValue(Menu entity) {
        if (entity == null) {
            return;
        }
        Menu parent = entity.getParent();
        if (parent != null) {
            entity.setFullName(parent.getFullName() + entity.getTitle());
            entity.setTreePath(parent.getTreePath() + parent.getId() + Menu.TREE_PATH_SEPARATOR);
        } else {
            entity.setFullName(entity.getTitle());
            entity.setTreePath(Menu.TREE_PATH_SEPARATOR);
        }
        entity.setGrade(entity.getParentIds().length);
    }
}
