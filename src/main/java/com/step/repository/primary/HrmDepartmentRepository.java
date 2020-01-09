package com.step.repository.primary;

import com.step.entity.primary.HrmDepartment;
import org.springframework.cache.annotation.CacheConfig;
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
@Transactional("transactionManagerPrimary")
public interface HrmDepartmentRepository extends JpaSpecificationExecutor<HrmDepartment>,JpaRepository<HrmDepartment,Long>{
    /***
     * 根据部门id 获取所有部门
     * @param cids 公司id
     * @return list
     */
    @Query(nativeQuery = true,value = "with subqry(id,departmentname,supdepid,tlevel,canceled,showorder,subcompanyid1) as(\n" +
            "   select id,departmentname,supdepid,tlevel,canceled,showorder,subcompanyid1 from HrmDepartment where (canceled is null or canceled = 0) and subcompanyid1 in ?1\n" +
            "   union all\n" +
            "   select HrmDepartment.id,HrmDepartment.departmentname,HrmDepartment.supdepid,HrmDepartment.tlevel,HrmDepartment.canceled,HrmDepartment.showorder,HrmDepartment.subcompanyid1\n" +
            "   from HrmDepartment,subqry where HrmDepartment.supdepid = subqry.id and (HrmDepartment.canceled is null or HrmDepartment.canceled =0)\n" +
            ") select DISTINCT * from subqry order by tlevel asc, showorder asc")
    List<HrmDepartment> findChildren(List<Long> cids);

    @Query(nativeQuery = true,value = "select H.departmentid from HrmResource H where loginid = ?1")
    Long findByUserId(String userId);

    @Query(nativeQuery = false,value = "select D from HrmDepartment D  join fetch D.hrmSubCompany p where D.id = ?1")
    HrmDepartment findDepartmentById(Long bumen);
}
