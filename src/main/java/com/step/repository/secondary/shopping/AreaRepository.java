package com.step.repository.secondary.shopping;

import com.step.entity.secondary.RoleType;
import com.step.entity.secondary.shopping.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * shigz
 * 2020/1/9
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface AreaRepository extends JpaRepository<Area,Long>, JpaSpecificationExecutor<Area> {
}
