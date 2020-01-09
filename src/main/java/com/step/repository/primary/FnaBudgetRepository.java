package com.step.repository.primary;

import com.step.entity.primary.FnaExpenseInfo;
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

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 * 预算控制
 */
@Repository
@Transactional(value = "transactionManagerPrimary")
@CacheConfig(cacheNames = "dbCache")
public interface FnaBudgetRepository  extends JpaSpecificationExecutor<FnaExpenseInfo>,JpaRepository<FnaExpenseInfo,Long>{
    @Cacheable
    @Query(nativeQuery = true,value = "select ctrlset from CptCtrlSet")
    Integer isCptCtrlSet();

    /***
     * 获取费用年id
     * @param requestDate  申请日期
     * @return
     */
    @Cacheable
    @Query(nativeQuery = true,value = "select id from FnaYearsPeriods where ?1 >= startdate  and ?1 <= enddate ")
    Integer getFnaYearId(String requestDate);
    @Query(nativeQuery = true,
            value = "select case when SUM(amount) IS null then 0 else SUM(amount) end  amount from FnaExpenseInfo " +
                    "where status =:status and subject =:kemu and organizationtype = 2 " +
                    "and organizationid =:bumen and occurdate >=:startdate and occurdate <=:enddate" )
    BigDecimal getFnaExpenseInfoSumAmount(@Param("kemu") String kemu, @Param("bumen")String bumen,
                                          @Param("startdate")String startdate,
                                          @Param("enddate")String enddate,
                                          @Param("status")int status);

    /***
     * 获取科目名称
     * @param budgetAccountBizCode 科目号
     * @return string
     */
    @Cacheable
    @Query(nativeQuery = true,value = "select name from FnaBudgetfeeType where id=?1 ")
    String getSubjectName(String budgetAccountBizCode);
    @Cacheable
    @Query(nativeQuery = true,value = "select ifcontrol from fnacontrolset where id =1")
    Integer isFnaController();

    /***
     * 根据预算编码释放预算F
     * @param budgetCode 预算编码
     */
    @Modifying
    @Query(nativeQuery = false,value = "delete  from FnaExpenseInfo where guid = ?1 and status = '0' ")
    void deleteByBudgetCode(String budgetCode);
    @Query(nativeQuery = false,value = "select F from FnaExpenseInfo F where guid = ?1 ")
    FnaExpenseInfo getOneByBudgetCode(String budgetCode);

}
