package com.step.service.admin.impl;

import com.step.dao.RoleTypeDao;
import com.step.entity.secondary.RoleType;
import com.step.repository.secondary.RoleRepository;
import com.step.repository.secondary.RoleTypeRepository;
import com.step.service.admin.RoleTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class RoleTypeServiceImpl extends BaseServiceImpl<RoleType,Long> implements RoleTypeService {
    @Autowired
    private RoleTypeRepository roleTypeRepository;
    @Autowired
    private RoleTypeDao roleDao;
    /**
     * 分页获取所有的角色类型
     */
    @Override
    public  Page<RoleType> findPage(Example<RoleType> example, Pageable pageable) {
        Page<RoleType> rolePag = roleTypeRepository.findAll(example,pageable);
        return rolePag;
    }

    /**
     * 获取所有的角色类型
     */
    @Override
    public List<RoleType> findAll() {
        List<RoleType> roleTypes = roleTypeRepository.findAll(new Sort(Sort.Direction.ASC,"order"));
        return roleTypes;
    }
    /***
     * 修改
     * @param entity
     * @return
     */
    @Override
    public RoleType update(RoleType entity) {
        Assert.notNull(entity);
        return roleDao.merge(entity);
    }

    @Override
    public boolean codeExists(String code) {
        return  roleDao.exists("code", code, true);
    }

    @Override
    public RoleType create(RoleType entity) {
        Assert.notNull(entity,"对象不能为空");
        return roleTypeRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        roleTypeRepository.deleteById(id);
    }

    /**
     * 根据Id 获取Role 对象
     * @param id
     * @return
     */
    @Override
    public RoleType findById(Long id) {
        Optional<RoleType> roleType =  roleTypeRepository.findById(id);
        if(roleType.isPresent()){
            return roleType.get();
        }
        return null;
    }

    @Override
    public RoleType findByCode(String code) {
        Assert.notNull(code,"角色类型code不能为空");
        RoleType roleType= roleTypeRepository.findByCode(code);
        if(roleType==null){
            throw  new RuntimeException("没有对应的角色类型");
        }
        return roleType;
    }
}
