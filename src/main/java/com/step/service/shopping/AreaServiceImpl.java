package com.step.service.shopping;

import com.step.entity.secondary.shopping.Area;
import com.step.entity.secondary.shopping.Filter;
import com.step.repository.secondary.shopping.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * shigz
 * 2020/1/9
 **/
@Service
public class AreaServiceImpl  implements  AreaService{
    @Autowired
    private AreaRepository areaRepository;
    @Override
    public Area find(Long aLong) {
        return null;
    }

    @Override
    public List<Area> findAll() {
        return null;
    }

    @Override
    public List<Area> findList(Long... longs) {
        return null;
    }

    @Override
    public List<Area> findList(Integer count, List<Filter> filters) {
        return null;
    }

    @Override
    public List<Area> findList(Integer first, Integer count, List<Filter> filters) {
        return null;
    }

    @Override
    public Page<Area> findPage(Pageable pageable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public long count(Filter... filters) {
        return 0;
    }

    @Override
    public boolean exists(Long aLong) {
        return false;
    }

    @Override
    public boolean exists(Filter... filters) {
        return false;
    }

    @Override
    public Area save(Area entity) {
        return null;
    }

    @Override
    public Area update(Area entity) {
        return null;
    }

    @Override
    public Area update(Area entity, String... ignoreProperties) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void delete(Long... longs) {

    }

    @Override
    public void delete(Area entity) {

    }

    @Override
    public List<Area> findRoots() {
        List<Area> areaList = areaRepository.findAll();
        return areaList;
    }
}
