package com.step.service.admin.impl;

import com.google.common.collect.Lists;
import com.step.entity.secondary.Element;
import com.step.entity.secondary.Menu;
import com.step.entity.secondary.ResourceAuthority;
import com.step.entity.secondary.Role;
import com.step.repository.secondary.ElementRepository;
import com.step.repository.secondary.MenuRepository;
import com.step.repository.secondary.ResourceAuthorityRepository;
import com.step.repository.secondary.RoleRepository;
import com.step.service.admin.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.swing.text.html.HTMLDocument;
import java.util.*;

/**
 * Created by zhushubin  on 2019-11-13.
 * email:604580436@qq.com
 */
@Service
@Transactional("transactionManagerSecondary")
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ElementRepository elementRepository;
    @Autowired
    private ResourceAuthorityRepository resourceAuthorityRepository;

    /***
     * 绑定角色跟资源关系
     * @param roleId 角色id
     * @param menuIds 菜单id
     * @param elementIds 按钮或资源id
     */
    @Override
    @Transactional("transactionManagerSecondary")
    public void save(Long roleId, List<Long> menuIds, List<Long> elementIds) {
        Role role = roleRepository.getOne(roleId);
        List<Menu> menus = menuRepository.findAllById(menuIds);
        Set<Long> menuPids =new HashSet<>();
        List<Element> elements = elementRepository.findAllById(elementIds);
        List<ResourceAuthority> resourceAuthorities = Lists.newArrayList();

        menus.stream().forEach(e -> {
            ResourceAuthority menu = ResourceAuthority.buildMenu();
            menuPids.add(e.getParentId());
            menu.setRole(role);
            menu.setMenu(e);
            resourceAuthorities.add(menu);
        });
        List<Menu> pmenus=menuRepository.findAllById(menuPids);
        pmenus.stream().forEach(e -> {
            ResourceAuthority menu = ResourceAuthority.buildMenu();
            menuPids.add(e.getParentId());
            menu.setRole(role);
            menu.setMenu(e);
            resourceAuthorities.add(menu);
        });
        elements.stream().forEach(e -> {
            ResourceAuthority button = ResourceAuthority.buildButton();
            button.setRole(role);
            button.setElement(e);
            resourceAuthorities.add(button);

        });
        resourceAuthorityRepository.deleteByRoleId(role.getId());
        resourceAuthorityRepository.saveAll(resourceAuthorities);
    }

    /***
     *
     * @param roleIds 角色ids
     * @return list
     */
    @Override
    public List<ResourceAuthority> findByRoleIds(List<Long> roleIds) {
        Specification<ResourceAuthority> spc = new Specification<ResourceAuthority>() {
            @Override
            public Predicate toPredicate(Root<ResourceAuthority> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                CriteriaBuilder.In<Object> cbIn = cb.in(root.get("role"));
                root.fetch("menu", JoinType.LEFT);
                root.fetch("element", JoinType.LEFT);
                roleIds.stream().forEach(e -> {
                    cbIn.value(e);
                });
                return cb.and(cbIn);
            }
        };
        return resourceAuthorityRepository.findAll(spc);
    }
}
