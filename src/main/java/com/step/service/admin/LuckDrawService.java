package com.step.service.admin;

import com.step.entity.secondary.LuckNumber;

import java.util.List;

/**
 * shigz
 * 2019/12/20
 **/
public interface LuckDrawService {
    /**
     * 查询所有未锁定抽奖号
     * @return
     */
    List<LuckNumber> find();

    /**
     * 添加抽奖号
     * @param person
     */
    void add(Integer person);

    /**
     * 去除抽奖号
     * @param keys
     */
    void update(List<Integer> keys);
}
