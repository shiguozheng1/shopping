package com.step.repository.primary;

import com.step.entity.primary.ProjectFnaExpenseInfoItem;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 * 项目预算控制预算控制明细
 */
@Repository
@Transactional(value = "transactionManagerPrimary")
@CacheConfig(cacheNames = "dbCache")
public interface ProjectFnaExpenseItemRepository extends JpaRepository<ProjectFnaExpenseInfoItem,Long>{
}
