package com.step.service;

/**
 * Created by zhushubin  on 2019-06-27.
 * email:604580436@qq.com
 * 企业微信号服务
 */
public interface WxCpService {

    /**
     * 从数据库修改用户信息
     * @return
     */
    void modifyUsers();

    /**
     * 创建用户
     */
    void createUser();
}
