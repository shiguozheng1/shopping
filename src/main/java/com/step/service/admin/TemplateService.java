package com.step.service.admin;

import com.step.entity.secondary.workflow.SimpleTemplateEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author shiguozheng
 * @date 2019/11/20
 * 表单设计service
 */
public interface TemplateService {
    /**
     * 分页获取表单
     * @param example
     * @param pageable
     * @return
     */
    Page<SimpleTemplateEntity> findPage(Example<SimpleTemplateEntity> example, Pageable pageable);

    /**
     * 根据id查询表单
     * @param id
     * @return
     */
    SimpleTemplateEntity findById(Long id);

    /**
     * 检查编码是否存在
     * @param code
     * @return
     */
    boolean codeExists(String code);

    /**
     * 保存
     * @param entity
     * @return
     */
    SimpleTemplateEntity create(SimpleTemplateEntity entity);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 查询全部模板
     */
    List<SimpleTemplateEntity> findAll();

}
