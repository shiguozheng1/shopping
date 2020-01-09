package com.step.service.impl;

import com.step.entity.primary.HrmRoles;
import com.step.repository.primary.HrmRolesRepository;
import com.step.service.HrmRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhushubin  on 2019-09-16.
 * email:604580436@qq.com
 */
@Service
@Transactional("transactionManagerPrimary")
public class HrmRolesServiceImpl implements HrmRolesService {
    @Autowired
    private HrmRolesRepository hrmRolesRepository;

    @Override
    public List<HrmRoles> findByRolesNameLikeOrderByRolesNameAsc(String rolesName) {
        return hrmRolesRepository.findByRolesNameLikeOrderByRolesNameAsc(rolesName);
    }

    @Override
    public List<String> findMemberByRoleId(Long id) {
        return hrmRolesRepository.findMembersByRoleId(id);
    }
}
