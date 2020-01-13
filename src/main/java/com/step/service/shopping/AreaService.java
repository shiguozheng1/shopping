package com.step.service.shopping;

import com.step.entity.secondary.shopping.Area;

import java.util.List;

/**
 * shigz
 * 2020/1/9
 **/
public interface AreaService  extends BaseService<Area, Long> {
    /**
     * 查找顶级地区
     *
     * @return 顶级地区
     */
    List<Area> findRoots();
}
