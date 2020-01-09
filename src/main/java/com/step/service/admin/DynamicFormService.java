package com.step.service.admin;

import com.step.entity.secondary.workflow.DynamicFormEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by zhushubin  on 2019-11-21.
 * email:604580436@qq.com
 * 动态表单服务
 */
public interface DynamicFormService {
    public void  deploy(Long id);

    /***
     * 保存动态表单
     * @param bindEntity
     */
    void save(DynamicFormEntity bindEntity);

    /***
     * 分页查找动态表单
     * @param pageable
     * @return
     */
    Page<DynamicFormEntity> findByPage(Pageable pageable);

    /***
     * 删除表单
     * @param id
     */
    void delete(Long id);

    /**
     * 查询表单
     */
    DynamicFormEntity findById(Long id);

    /**
     * 查询所有表单
     * @return
     */
    List<DynamicFormEntity> findAll();
}
