package com.step.repository.primary;

import com.step.entity.primary.HrmResource;
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
public interface HrmResourceRepository extends JpaSpecificationExecutor<HrmResource>,JpaRepository<HrmResource,Long>{

    @Query(value = "select m from HrmResource m join fetch m.hrmDepartment  where status in (?1) and loginid <> ''")
    List<HrmResource> findByStatus(List<Integer> integers);
    @Query(nativeQuery = true,value = "select id from cus_fielddata where scopeid=-1 and field28='1'")
    List<Long> findAllowedUserIds();
}
