package com.step.service.admin;

import com.step.entity.secondary.Admin;
import com.step.entity.secondary.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhushubin  on 2019-10-30.
 * email:604580436@qq.com
 */

public interface UserService {
    /***
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    Admin findByUserName(String username);

    /**
     *
     * @param role
     * @param pageable
     * @return
     */
    Page<Admin> findByPage(Example<Admin> role, Pageable pageable);

    /**
     * 根据用户名查看用户是否存在
     * @param code
     * @return
     */
    boolean usernameExists(String code);

    /**
     * 创建用户
     * @param entity
     * @return
     */
    Admin create(Admin entity);

    /**
     * 查询用户
     * @param id
     * @return
     */
    Admin findById(Long id);

    /**
     * 更新
     * @param entity
     * @return
     */
    Admin update(Admin entity);

    /**
     * 删除
     * @param entity
     */
    void delete(Admin entity);
    /**
     * 获取当前登录用户
     *
     * @param userClass
     *            用户类型
     * @return 当前登录用户，若不存在则返回null
     */
   Admin getCurrent();
}
