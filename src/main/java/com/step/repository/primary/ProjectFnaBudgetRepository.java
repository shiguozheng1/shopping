package com.step.repository.primary;

import com.step.entity.primary.FnaExpenseInfo;
import com.step.entity.primary.ProjectFnaExpenseInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 * 项目预算控制预算控制
 */
@Repository
@Transactional(value = "transactionManagerPrimary")
@CacheConfig(cacheNames = "dbCache")
public interface ProjectFnaBudgetRepository extends JpaSpecificationExecutor<ProjectFnaExpenseInfo>,JpaRepository<ProjectFnaExpenseInfo,Long>{

    /***
     * 查找单个
     * @param sn
     * @return
     */
    @Query(nativeQuery = true,value = "select id from uf_zwxxmys F where F.xmh='BA003B006A'")
    ProjectFnaExpenseInfo findOneBySn(String sn);
}
