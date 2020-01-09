package com.step.repository.primary;

import com.step.entity.primary.FnaBudgetfeeType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhushubin  on 2019-10-30.
 * email:604580436@qq.com
 * 科目
 */
@Repository
@Transactional(value = "transactionManagerPrimary")
@CacheConfig(cacheNames = "dbCache")
public interface FnaBudgetfeeTypeRepository extends JpaSpecificationExecutor<FnaBudgetfeeType>,JpaRepository<FnaBudgetfeeType,Long> {
}
