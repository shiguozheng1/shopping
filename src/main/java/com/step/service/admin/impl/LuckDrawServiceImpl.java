package com.step.service.admin.impl;
import com.google.common.collect.Lists;
import com.step.entity.secondary.LuckNumber;
import com.step.repository.secondary.LuckDrawRepository;
import com.step.service.admin.LuckDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

/**
 * shigz
 * 2019/12/20
 **/
@Service
public class LuckDrawServiceImpl implements LuckDrawService {
    @Autowired
    private LuckDrawRepository luckDrawRepository;
    /**
     * 删除
     * @param keys
     */
    public void update(List<Integer> keys){
        List<LuckNumber> numbers = luckDrawRepository.findAllByKey(keys);
        List<LuckNumber> updates= numbers.stream().map(e->{
            e.setIsEnabled(true);
            return e;
        }).collect(Collectors.toList());
        luckDrawRepository.saveAll(updates);
    }

    /**
     * 添加
     * @param person
     */
    public void add(Integer person){
        List<LuckNumber> luckNumbers = Lists.newArrayList();
        int count = (int)luckDrawRepository.count();
        LuckNumber luckNumber;
        for(int i = 1+count ; i<= person ;i++) {
            luckNumber = new LuckNumber();
            luckNumber.setKey(i);
            luckNumbers.add(luckNumber);
        }
        luckDrawRepository.saveAll(luckNumbers);
    }

    /**
     * 查找
     * @return
     */
    public List<LuckNumber> find() {
        Specification<LuckNumber> spc = new Specification<LuckNumber>() {
            @Override
            public Predicate toPredicate(Root<LuckNumber> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate predicate = cb.and(cb.or(cb.notEqual(root.get("is_locked"), 1), cb.isNull(root.get("is_locked"))));
                return predicate;
            }
        };
        List<LuckNumber> ret = luckDrawRepository.findAll(spc);
        return ret;
    }
}
