package com.step.service.impl;

import com.step.entity.primary.HrmDepartment;
import com.step.repository.primary.HrmDepartmentRepository;
import com.step.service.HrmDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by user on 2019-04-18.
 */
@Service
@Transactional("transactionManagerPrimary")
public class HrmDepartmentServiceImpl implements HrmDepartmentService {
    @Autowired
    private HrmDepartmentRepository hrmDepartmentRepository;
    /***
     * 根据公司ids 获取所有部门
     * @param cids 公司id
     * @return list
     */
    @Override
    public List<HrmDepartment> findByCompanyIds(List<Long> cids) {
        return hrmDepartmentRepository.findChildren(cids);
    }
    @Transactional(value = "transactionManagerPrimary",readOnly = true)
    public  List<HrmDepartment> findAllPO(){
        Specification<HrmDepartment> spc =new Specification<HrmDepartment>() {
            @Override
                public Predicate toPredicate(Root<HrmDepartment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.and(cb.or(cb.notEqual(root.get("canceled"), 1), cb.isNull(root.get("canceled"))));
                return predicate;
            }
        };
        return hrmDepartmentRepository.findAll(spc);
    }
}
