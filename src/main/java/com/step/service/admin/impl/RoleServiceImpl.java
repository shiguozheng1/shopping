package com.step.service.admin.impl;
import com.step.dao.RoleDao;
import com.step.entity.secondary.ResourceAuthority;
import com.step.entity.secondary.Role;
import com.step.repository.secondary.RoleRepository;
import com.step.service.admin.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional("transactionManagerSecondary")
@Slf4j
public class RoleServiceImpl extends BaseServiceImpl<Role,Long> implements RoleService{

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleDao roleDao;
    /**
     * 递归查询角色
     * @param id
     * @return
     */
    @Override
    public List<Role> findAll(Long id) {
        return roleRepository.findAllByRoleTypeId(id);
    }

    /**
     * 根据id 获取角色
     * @param id
     * @return
     */
    @Override
    @Transactional(value = "transactionManagerSecondary",readOnly = true)
    public Role find(Long id) {
        Optional<Role> opt =  roleRepository.findById(id);
        if(opt.isPresent()){
           return opt.get();
        }
        return null;
    }

    @Override
    @Transactional(value = "transactionManagerSecondary",readOnly = true)
    public Role findByCode(String code) {
        Assert.notNull(code);
        Role role= roleRepository.findByCode(code);
        return role;
    }

    /***
     * 修改
     * @param entity
     * @return
     */
    @Override
    public Role update(Role entity) {
        Assert.notNull(entity);
        setValue(entity);
        return roleDao.merge(entity);
    }

    @Override
    @Transactional(value = "transactionManagerSecondary",readOnly = true)
    public boolean codeExists(String code) {
        return  roleDao.exists("code", code, true);
    }
    @Override
    public Role create(Role entity) {
        Assert.notNull(entity);
        setValue(entity);
        return roleRepository.save(entity);
    }

    /***
     * 查找所有角色
     * @param spc
     * @return
     */
    @Override
    public List<Role> findAll(Specification<Role> spc) {
           return roleRepository.findAll(spc);
    }

    /***
     * 删除角色
     * @param id
     */
    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    /***
     * 保存授权资源
     * @param roleId 角色id
     * @param menuIds 菜单ids
     * @param elementIds 按钮或资源id
     */
    @Override
    public void savePermissions(Long roleId, List<Long> menuIds, List<Long> elementIds) {
       Role role = roleRepository.getOne(roleId);
    }

    /**
     * 根据IDS查询Roles
     */
    public List<Role> findAddByIds(List<Long> roleIds){
        Specification<Role> spc= new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                CriteriaBuilder.In<Object> in = cb.in(root.get("id"));
                for(Long roleId:roleIds){
                    in.value(roleId);
                }
                return in;
            }
        };
        List<Role> roles = roleRepository.findAll(spc);
        return roles;
    }




    /***
     * 保存数据前计算父节点信息
     * @param entity
     */
    private void setValue(Role entity) {
        if (entity == null) {
            return;
        }
        Role parent = entity.getParent();
        if (parent != null) {
            entity.setTreePath(parent.getTreePath() + parent.getId() + Role.TREE_PATH_SEPARATOR);
        } else {
            entity.setTreePath(Role.TREE_PATH_SEPARATOR);
        }
        entity.setGrade(entity.getParentIds().length);
    }
}
