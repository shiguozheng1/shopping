package com.step.repository.primary;

import com.step.entity.primary.HrmSubCompany;
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
public interface HrmSubCompanyRepository extends JpaSpecificationExecutor<HrmSubCompany>,JpaRepository<HrmSubCompany,Long>{
    /***
     * 查账当前节点所有子节点
     * @return
     */
    @Query(nativeQuery = true,value = "with subqry(id,subcompanyname,supsubcomid,tlevel,canceled,showorder) as(\n" +
            "   select id,subcompanyname,supsubcomid,tlevel,canceled,showorder from HrmSubcompany where (canceled is null or canceled = 0) and id=?1\n" +
            "   union all\n" +
            "   select HrmSubcompany.id,HrmSubcompany.subcompanyname,HrmSubcompany.supsubcomid,HrmSubcompany.tlevel,HrmSubcompany.canceled,HrmSubcompany.showorder\n" +
            "   from HrmSubcompany,subqry where HrmSubcompany.supsubcomid = subqry.id and (HrmSubcompany.canceled is null or HrmSubcompany.canceled=0)\n" +
            ") select * from subqry order by tlevel asc, showorder asc")
    List<HrmSubCompany> findChildren(Long id);
    @Query(nativeQuery=false,value = "select H from HrmSubCompany H  where id=?1")
    HrmSubCompany findOneById(Long id);

}
