package com.step.service.admin.impl;

import com.step.entity.secondary.Element;
import com.step.repository.secondary.ElementRepository;
import com.step.service.admin.ElementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by zhushubin  on 2019-11-06.
 * email:604580436@qq.com
 * 按钮或资源-services
 */
@Service
@Transactional("transactionManagerSecondary")
@Slf4j
public class ElementServiceImpl extends BaseServiceImpl<Element,Long> implements ElementService{
    @Autowired
    private ElementRepository elementRepository;
    /**
     * 判断编号是否存在
     *
     * @param code
     *            编号(忽略大小写)
     * @return 编号是否存在
     */
    @Override
    public boolean codeExists(String code) {
        Element element = new Element();
        element.setCode(code);
        Example<Element> example= Example.of(element);
        boolean flag =   elementRepository.exists(example);
        return flag;
    }

    @Override
    public Element save(Element entity) {
        return elementRepository.save(entity);
    }

    /***
     *
     * @param example
     * @param pageRequest 分页请求
     */
    @Override
    public Page<Element> findPage(Example<Element> example, PageRequest pageRequest) {
      Page<Element> page=  elementRepository.findAll(example,pageRequest);
      return page;
    }

    @Override
    public Element find(Long id) {
       Optional<Element> opt= elementRepository.findById(id);
       if(opt.isPresent()){
           return opt.get();
       }
        return null;
    }

    @Override
    public Element findByCode(String code) {
        return elementRepository.findByCode(code);
    }

    @Override
    public void delete(Long id) {
        elementRepository.deleteById(id);
    }
}
