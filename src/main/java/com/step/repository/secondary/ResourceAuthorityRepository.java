package com.step.repository.secondary;

import com.step.entity.secondary.ResourceAuthority;
import com.step.entity.secondary.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * shigz
 * 2019/10/25
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface ResourceAuthorityRepository extends JpaRepository<ResourceAuthority,Long> , JpaSpecificationExecutor<ResourceAuthority> {

    /***
     * 根据角色id 删除记录
     * @param id 角色id
     */
    @Modifying
    @Query("delete from ResourceAuthority where  role.id= ?1")
    void deleteByRoleId(Long id);
}
