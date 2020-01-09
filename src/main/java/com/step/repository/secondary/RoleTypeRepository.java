package com.step.repository.secondary;

import com.step.entity.secondary.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * shigz
 * 2019/10/25
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface RoleTypeRepository extends JpaRepository<RoleType,Long> , JpaSpecificationExecutor<RoleType> {
    @Query(value = "select r from RoleType r join fetch r.roles where r.code=:code")
    RoleType findByCode(@Param("code") String code);
}
