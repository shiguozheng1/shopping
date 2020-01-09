package com.step.repository.primary;

import com.step.entity.primary.HrmDepartmentDefined;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by user on 2019-04-17.
 *
 */
@Repository
@Transactional(value = "transactionManagerPrimary")
@CacheConfig(cacheNames = "dbCache")
public interface HrmDepartmentDefinedRepository extends JpaSpecificationExecutor<HrmDepartmentDefined>,JpaRepository<HrmDepartmentDefined,Long>{
    List<HrmDepartmentDefined> findByDeptIdIn(List depids);

}
