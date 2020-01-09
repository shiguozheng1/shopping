package com.step.service.admin;

import com.step.entity.secondary.Element;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author zhushubin
 * @date 2019-11-06
 * email:604580436@qq.com
 */
public interface ElementService extends BaseService<Element,Long> {
    /**
     * 判断编号是否存在
     *
     * @param code
     *            编号(忽略大小写)
     * @return 编号是否存在
     */
    boolean codeExists(String code);

    /***
     * 新增按钮或资源
     * @param entity
     * @return
     */
    Element save(Element entity);

    /***
     * 分页查找按钮或资源
     * @param example
     * @param pageRequest 分页请求
     */
    Page<Element> findPage(Example<Element> example, PageRequest pageRequest);

    /***
     * 查找按钮或资源
     * @param id
     * @return
     */
    Element find(Long id);

    /***
     * 根据code 查找按钮或资源
     * @param code
     * @return
     */
    Element findByCode(String code);

    /***
     * 删除
     * @param id
     */
    void delete(Long id);
}
