package com.step.service.admin.impl;

import com.step.service.admin.FlowableModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhushubin  on 2019-11-27.
 * email:604580436@qq.com
 */
@Service
@Transactional("transactionManagerSecondary")
@Slf4j
public class FlowableModelServiceImpl implements FlowableModelService {
}
