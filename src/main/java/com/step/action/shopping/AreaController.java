package com.step.action.shopping;
import com.google.common.collect.Lists;
import com.step.entity.secondary.shopping.Area;
import com.step.entity.secondary.vo.shopping.AreaVo;
import com.step.service.shopping.AreaService;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * shigz
 * 2020/1/9
 **/
@RestController("adminAreaController")
@RequestMapping("/admin/area")
public class AreaController {
    @Autowired
    private AreaService areaService;
    /**
     * 添加
     */
    @GetMapping("/list")
    public Wrapper find(Long parentId) {
        Area parent = areaService.find(parentId);
        if(parent!=null){
            Set<Area> children = parent.getChildren();
            return WrapMapper.ok(children);
        }else{
            List<Area> roots = areaService.findRoots();
            List<AreaVo> ret = Lists.newArrayList();
            roots.stream().forEach(e->{
                AreaVo vo = new AreaVo();
                BeanUtils.copyProperties(e,vo);
                vo.setId(e.getId());
                vo.setCreateDate(e.getCreatedDate());
                vo.setLastModifyDate(e.getLastModifiedDate());
                ret.add(vo);
            });
            return WrapMapper.ok(ret);
        }
    }

    @DeleteMapping
    public Wrapper delete(Long id){
        areaService.delete(id);
        return WrapMapper.ok();
    }
}
