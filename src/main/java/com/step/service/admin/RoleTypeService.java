package com.step.service.admin;


import com.step.entity.secondary.RoleType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by zhushubin  on 2019-06-03.
 * email:604580436@qq.com
 */
public interface RoleTypeService extends BaseService<RoleType,Long>{
    Page<RoleType> findPage(Example<RoleType> role, Pageable pageable);

    boolean codeExists(String code);

    RoleType create(RoleType entity);

    void deleteById(Long id);

    RoleType findById(Long id);

    RoleType findByCode(String code);

    RoleType update(RoleType entity);

    List<RoleType> findAll();
}