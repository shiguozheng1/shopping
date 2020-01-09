package com.step.service.impl;

import com.step.entity.primary.HrmSubCompany;
import com.step.repository.primary.HrmSubCompanyRepository;
import com.step.service.HrmSubCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * Created by user on 2019-04-17.
 */
@Service
@Transactional("transactionManagerPrimary")
public class HrmSubCompanyServiceImpl implements HrmSubCompanyService {
    @Autowired
    private HrmSubCompanyRepository hrmSubCompanyRepository;
    @Override
    @Transactional(value = "transactionManagerPrimary",readOnly = true)
    public List<HrmSubCompany> findRoots() {
        Specification<HrmSubCompany> spc= new Specification<HrmSubCompany>() {
            @Override
            public Predicate toPredicate(Root<HrmSubCompany> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate=null;
                predicate=cb.and(cb.or(cb.notEqual(root.get("canceled"),1),cb.isNull(root.get("canceled"))),cb.or(cb.isNull(root.get("parent")),cb.equal(root.get("parent"),0)));
                return predicate;
            }
        };

        return hrmSubCompanyRepository.findAll(spc);
    }

    @Transactional(value = "transactionManagerPrimary",readOnly = true)
    @Override
    public  List<HrmSubCompany> findAll(){
        return hrmSubCompanyRepository.findAll();
    }

    @Override
    @Transactional(value = "transactionManagerPrimary",readOnly = true)
    public HrmSubCompany findById(Long cid) {
     HrmSubCompany hrmSubCompany=hrmSubCompanyRepository.findOneById(cid);
     return hrmSubCompany;
    }


    /***
     * 获取所有孩子子节点
     * @param parent 父节点
     * @param recursive 是否递归
     * @param count 数量
     * @return list
     */
    @Override
    @Transactional(value = "transactionManagerPrimary",readOnly = true)
    public List<HrmSubCompany> findChildren(HrmSubCompany parent, boolean recursive, Integer count) {
        return hrmSubCompanyRepository.findChildren(parent.getId());
    }

    /***
     * 根据id 查找
     * @param ids
     * @return
     */


}
