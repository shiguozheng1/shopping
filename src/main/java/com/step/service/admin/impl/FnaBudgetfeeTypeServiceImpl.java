package com.step.service.admin.impl;

import com.step.entity.primary.FnaBudgetfeeType;
import com.step.repository.primary.FnaBudgetfeeTypeRepository;
import com.step.service.admin.FnaBudgetfeeTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhushubin  on 2019-10-30.
 * email:604580436@qq.com
 */
@Service
@Transactional("transactionManagerPrimary")
@Slf4j
public class FnaBudgetfeeTypeServiceImpl  implements FnaBudgetfeeTypeService {
    @Autowired
    private FnaBudgetfeeTypeRepository repository;
    @Override
    public List<FnaBudgetfeeType> findAll() {
        return repository.findAll();
    }
}
