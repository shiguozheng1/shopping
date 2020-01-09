package com.step.repository.secondary;

import com.step.entity.secondary.LuckNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * shigz
 * 2019/12/20
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface LuckDrawRepository extends JpaSpecificationExecutor<LuckNumber>, JpaRepository<LuckNumber,Long> {
    List<LuckNumber> findAllByKey(List<Integer> keys);
    long count();
}
