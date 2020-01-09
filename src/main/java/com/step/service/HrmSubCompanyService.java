package com.step.service;

import com.step.entity.primary.HrmSubCompany;

import java.util.List;

/**
 * Created by user on 2019-04-17.
 */
public interface
HrmSubCompanyService {
    /***
     * 查找根节点
     * @return list
     */
    List<HrmSubCompany> findRoots();

    /***
     * 根据父节点，获取所有孩子节点
     * @param parent
     * @param recursive 是否递归
     * @param count 数量
     * @return list
     */
    List<HrmSubCompany> findChildren(HrmSubCompany parent,boolean recursive, Integer count);

    /**
     * 查询所以节点
     * @return
     */
    public  List<HrmSubCompany> findAll();

    /***
     * 根据ID 查找公司
     * @param cid 公司id
     * @return list
     */
    HrmSubCompany findById(Long cid);
}
