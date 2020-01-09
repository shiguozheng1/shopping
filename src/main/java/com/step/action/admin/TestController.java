package com.step.action.admin;

import com.step.dao.FormSqlDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhushubin  on 2019-11-18.
 * email:604580436@qq.com
 */
@RestController("adminTestController")
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    FormSqlDao formSqlDao;
    @GetMapping
    public String test(){
        formSqlDao.createTable(null);
        return "";
    }
}
