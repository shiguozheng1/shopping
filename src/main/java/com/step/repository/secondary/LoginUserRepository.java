package com.step.repository.secondary;

import com.step.entity.secondary.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * shigz
 * 2019/10/25
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface LoginUserRepository extends JpaSpecificationExecutor<Admin>,JpaRepository<Admin,Long> {
    @Query(value = "select o from Admin o  left join fetch  o.roles where o.username=?1")
    Admin findByUsername(String username);
}
