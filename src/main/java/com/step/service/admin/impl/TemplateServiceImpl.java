package com.step.service.admin.impl;

import com.step.dao.TemplateDao;
import com.step.entity.secondary.workflow.SimpleTemplateEntity;
import com.step.repository.secondary.TemplateRepository;
import com.step.service.admin.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author shiguozheng
 * @date 2019-11-20
 * 模板管理
 */
@Service
@Transactional("transactionManagerSecondary")
@Slf4j
public class TemplateServiceImpl extends BaseServiceImpl<SimpleTemplateEntity,Long>  implements TemplateService {
  @Autowired
  private TemplateRepository templateRepository;
   @Autowired
   private TemplateDao templateDao;
    /**
     * 分页获取所有设计表
     */
    @Override
    public Page<SimpleTemplateEntity> findPage(Example<SimpleTemplateEntity> example, Pageable pageable) {
        Page<SimpleTemplateEntity> ret= templateRepository.findAll(example,pageable);
        return ret;
    }


    /**
     * 通过id查找表单
     * @param id
     * @return
     */
    @Override
    public SimpleTemplateEntity findById(Long id){
        Optional<SimpleTemplateEntity> result = templateRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        return null;
    }

    /**
     * 名字是否存在
     * @param code
     * @return
     */
    @Override
    public boolean codeExists(String code) {
        return  templateDao.exists("code", code, true);
    }

    /**
     * 更新表单
     * @param entity
     * @return
     */
    @Override
    public SimpleTemplateEntity create(SimpleTemplateEntity entity) {
        Assert.notNull(entity,"对象不能为空");
        return templateRepository.save(entity);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(Long id) {
        templateRepository.deleteById(id);
    }

    /**
     * 查询
     */
    public List<SimpleTemplateEntity> findAll(){
        return templateRepository.findAll();
    }
}
