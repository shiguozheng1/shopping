package com.step.service.admin.impl;

import com.step.dao.UserDao;
import com.step.entity.secondary.Admin;
import com.step.entity.secondary.Role;
import com.step.repository.secondary.LoginUserRepository;
import com.step.service.admin.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 *
 * @author zhushubin
 * @date 2019-10-30
 * email:604580436@qq.com
 */
@Service
@Transactional("transactionManagerSecondary")
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<Admin,Long> implements UserService {
    @Autowired
    private LoginUserRepository repository;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(value = "transactionManagerSecondary", readOnly = true)
    public Admin findByUserName(String username) {
        Specification<Admin> spc = new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicatesList = new ArrayList<>();
                if (StringUtils.isNotEmpty(username)) {
                    Predicate namePredicate = cb.equal(root.get("username"), username);
                    predicatesList.add(namePredicate);
                }
                SetJoin<Admin, Role> roles = root.join(root.getModel().getSet("roles", Role.class), JoinType.LEFT);
                Predicate[] predicates = new Predicate[predicatesList.size()];
                return cb.and(predicatesList.toArray(predicates));
            }
        };
        Optional<Admin> opt = repository.findOne(spc);
        if (opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    @Override
    public Page<Admin> findByPage(Example<Admin> example, Pageable pageable) {
        Page<Admin> admins = repository.findAll(example, pageable);
        return admins;
    }

    @Override
    public boolean usernameExists(String code) {
        return userDao.exists("username", code, true);
    }

    @Override
    public Admin create(Admin entity) {
        return repository.save(entity);
    }

    /**
     * 通过id查找用户
     *
     * @param id
     * @return
     */
    @Override
    public Admin findById(Long id) {
        Optional<Admin> result = repository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    /***
     * 修改
     * @param entity
     * @return
     */
    @Override
    public Admin update(Admin entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(Admin entity) {
        repository.delete(entity);
    }

    @Override
    public Admin getCurrent() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        Admin user = null;
        if (auth != null && auth.getPrincipal() != null) {
            String principal = "";
            if (auth.getPrincipal() instanceof String) {
                principal = auth.getPrincipal().toString();

                String pattern = "[1-9]\\d*";
                boolean isMatch = Pattern.matches(pattern, principal);
                try {
                    if (isMatch) {
                       Optional<Admin> opt = repository.findById(Long.parseLong(principal));
                       if(opt.isPresent()){
                           user = opt.get();
                       }
                    } else {
                        user = repository.findByUsername(principal);
                    }
                }catch (Exception e){
                    log.error(e.getMessage());
//                    try {
//                        user = repository.f("mobile", principal);
//                    }catch (Exception e1){
//                        log.error(e1.getMessage());
//                    }
                }

            } else if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User tmpUser = (org.springframework.security.core.userdetails.User) (auth.getPrincipal());
//                if(tmpUser instanceof UserInfo){
//                    if(((UserInfo) tmpUser).getUserType().equals(UserInfo.UserType.business)){
//                        user=businessDnull
            }


        }
        return user;
    }
}