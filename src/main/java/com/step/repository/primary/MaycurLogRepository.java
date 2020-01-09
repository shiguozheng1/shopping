package com.step.repository.primary;

import com.step.entity.primary.MaycurLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 * 预算控制操作日志
 */
@Repository
@Transactional("transactionManagerPrimary")
public interface MaycurLogRepository extends JpaRepository<MaycurLog,String> {
}
