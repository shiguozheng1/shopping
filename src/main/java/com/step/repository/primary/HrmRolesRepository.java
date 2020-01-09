package com.step.repository.primary;

import com.step.entity.primary.HrmResource;
import com.step.entity.primary.HrmRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by user on 2019-04-17.
 *
 */
@Repository
@Transactional(value = "transactionManagerPrimary")
public interface HrmRolesRepository extends JpaSpecificationExecutor<HrmRoles>,JpaRepository<HrmRoles,Long>{
    List<HrmRoles> findByRolesNameLikeOrderByRolesNameAsc(String rolesName);
    @Query(nativeQuery = true,value = "select resourceid from HrmRoleMembers where roleid = ?1")
    List<String> findMembersByRoleId(Long id);
}
