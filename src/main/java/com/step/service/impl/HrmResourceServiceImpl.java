package com.step.service.impl;

import com.step.entity.primary.HrmResource;
import com.step.repository.primary.HrmResourceRepository;
import com.step.service.HrmResourceService;
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
public class HrmResourceServiceImpl implements HrmResourceService {
    @Autowired
    private HrmResourceRepository hrmResourceRepository;
    @Override
    @Transactional(value = "transactionManagerPrimary",readOnly = true)
    public List<HrmResource> findByDepartmentIds(List<Long> depids) {
        Specification<HrmResource> spc= new Specification<HrmResource>() {
            @Override
            public Predicate toPredicate(Root<HrmResource> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate =null;
                CriteriaBuilder.In<Object> statusIN = cb.in(root.get("status"));
                statusIN.value(0);
                statusIN.value(1);
                statusIN.value(2);
                statusIN.value(3);
                Predicate predicateOr= null;
                CriteriaBuilder.In<Object> hrmDepartmentIN = cb.in(root.get("hrmDepartment"));
                for(Long depId:depids){
                    hrmDepartmentIN.value(depId);
                }

                predicate = cb.and(cb.isNotNull(root.get("loginid")),statusIN,hrmDepartmentIN,cb.notEqual(root.get("loginid"),""));
                return predicate;
            }
        };
        return hrmResourceRepository.findAll(spc);
    }

    @Override
    public List<HrmResource> findAll() {

        Specification<HrmResource> spc= new Specification<HrmResource>() {
            @Override
            public Predicate toPredicate(Root<HrmResource> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate =null;
                CriteriaBuilder.In<Object> statusIN = cb.in(root.get("status"));
                statusIN.value(0);
                statusIN.value(1);
                statusIN.value(2);
                statusIN.value(3);
                root.fetch("hrmDepartment");
                predicate = cb.and(cb.isNotNull(root.get("loginid")),
                        statusIN,cb.notEqual(root.get("loginid"),""));
                return predicate;
            }
        };
        return hrmResourceRepository.findAll(spc);
    }

    @Override
    public List<Long> findCusFielddata() {
        return hrmResourceRepository.findAllowedUserIds();
    }


    @Override
    @Transactional(readOnly = true)
    public List<HrmResource> findByCompanyIds(List<Long> cids) {
        Specification<HrmResource> spc= new Specification<HrmResource>() {
            @Override
            public Predicate toPredicate(Root<HrmResource> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate =null;
                CriteriaBuilder.In<Object> statusIN = cb.in(root.get("status"));
                statusIN.value(0);
                statusIN.value(1);
                statusIN.value(2);
                statusIN.value(3);
                CriteriaBuilder.In<Object> hrmSubCompanyIN = cb.in(root.get("hrmSubCompany"));
                for(Long depId:cids){
                    hrmSubCompanyIN.value(depId);
                }
                root.fetch("hrmDepartment");
                root.fetch("manager");
                predicate = cb.and(cb.isNotNull(root.get("loginid")),
                        statusIN,
                        hrmSubCompanyIN,
                        cb.notEqual(root.get("loginid"),""),
                        cb.isNotNull(root.get("workcode")),
                        cb.notEqual(root.get("workcode"),""));
                return predicate;
            }
        };
        return hrmResourceRepository.findAll(spc);
    }


    public List<HrmResource> findByIds(List<Long> ids){
        List<HrmResource> result= hrmResourceRepository.findAllById(ids);
        return result;
    }
}
