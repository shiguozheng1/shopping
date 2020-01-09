package com.step.action.admin;

import com.google.common.collect.Lists;
import com.step.entity.secondary.LuckNumber;
import com.step.service.admin.LuckDrawService;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * shigz
 * 2019/12/20
 **/
@RestController("adminLuckDrawController")
@RequestMapping("/admin/luckdraw")
public class LuckDrawController {
    @Autowired
    private LuckDrawService luckDrawService;

    /**
     * 新增抽奖号
     * @return
     */
    @PostMapping
    public Wrapper add(Integer person){
        luckDrawService.add(person);
        return WrapMapper.ok();
    }

    /**
     * 去除抽奖号
     */
    @PutMapping
    public Wrapper update(List<Integer> keys){
        luckDrawService.update(keys);
        return WrapMapper.ok();
    }

    /**
     * 查找
     */
    @GetMapping
    public Wrapper<List<LuckNumber>> find(){
        List<LuckNumber> ret = luckDrawService.find();
        return WrapMapper.ok(ret);
    }
}
