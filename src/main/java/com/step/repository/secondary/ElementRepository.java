package com.step.repository.secondary;

import com.step.entity.secondary.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * shigz
 * 2019/10/25
 **/
@Repository
@Transactional("transactionManagerSecondary")
public interface ElementRepository extends JpaRepository<Element,Long> {
    Element findByCode(String code);
}
