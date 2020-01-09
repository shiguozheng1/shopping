package com.step.repository.secondary;

import com.step.entity.secondary.MaycurToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * shigz
 * 2019/9/10
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface MaycurTokenRepository extends JpaRepository<MaycurToken,String> {
}
