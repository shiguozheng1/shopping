package com.step.action.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.step.action.BaseController;
import com.step.entity.secondary.RoleType;
import com.step.entity.secondary.vo.RoleTypeVo;
import com.step.service.admin.RoleTypeService;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;

/**
 * Created by zhushubin  on 2019-11-04.
 * email:604580436@qq.com
 */
@RestController("adminRoleTypeController")
@RequestMapping("/admin/role/type")
@Slf4j
public class RoleTypeController extends BaseController {
    @Autowired
    private RoleTypeService roleService;
    /**
     * 查询任务
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/page")
    public Wrapper queryByPage(@RequestParam(value = "pageNum") Integer pageNum,
                               @RequestParam(value = "pageSize") Integer pageSize,
                             String name) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC, "id");
        Page<RoleTypeVo> ret=null;
        try{
            RoleType roleType = new RoleType();
            roleType.setName(name);
            roleType.setIsSystem(null);
            ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name",ExampleMatcher.GenericPropertyMatchers.contains()) ;//构建对象
            Example<RoleType> example = Example.of(roleType,matcher);
            Page<RoleType> result = roleService.findPage(example,pageable);
            ret=result.map(new Function<RoleType, RoleTypeVo>() {
                @Override
                public RoleTypeVo apply(RoleType roleType) {
                    RoleTypeVo vo  = new RoleTypeVo();
                    BeanUtils.copyProperties(roleType,vo);
                    vo.setId(roleType.getId());
                    return vo;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return WrapMapper.error(e.getMessage());
        }
        return WrapMapper.ok(ret);
    }

    /**
     *获取角色类型
     * @return
     */
    @GetMapping("/all")
    @JsonView(RoleType.RoleTypeView.class)
    public Wrapper query() {
        List<RoleType> result;
        try{
            result = roleService.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return WrapMapper.error(e.getMessage());
        }
        return WrapMapper.ok(result);
    }


    @GetMapping
    public Wrapper getById(Long id){
        Assert.notNull(id,"id 不允许为空");
        RoleType  roleType = roleService.findById(id);
        if(roleType==null){
            return WrapMapper.error("无法获取到角色类型信息");
        }
        return WrapMapper.ok(roleType);
    }

    @GetMapping("/code")
    public Wrapper getByCode(String code){
        Assert.notNull(code,"code 不允许为空");
        RoleType  roleType = roleService.findByCode(code);
        if(roleType==null){
            return WrapMapper.error("无法获取到角色类型信息");
        }
        return WrapMapper.ok(roleType);
    }

    /***
     * 修改
     * @param entity
     * @return
     */
    @PutMapping
    public Wrapper put(@RequestBody RoleType entity){
        Assert.notNull(entity.getCode());
        RoleType tmpMenu= roleService.findByCode(entity.getCode());
        if(tmpMenu!=null){
            if(tmpMenu.getCode().equals(entity.getCode())){
                return  WrapMapper.error("角色类型已被占用！");
            }
        }
        try {
            roleService.update(entity);
        }catch (Exception e){
            log.error(e.getMessage());
            return WrapMapper.error("更新失败");
        }
        return WrapMapper.ok();
    }


    /**
     * 检查编号是否存在
     */
    @GetMapping("/check_code")
    public Wrapper  checkCode(String code) {
        if(StringUtils.isEmpty(code)) {
            WrapMapper.error("角色编码部门为空");
        }
        try{
            boolean  flag= roleService.codeExists(code);
            if(!flag){
                return WrapMapper.ok();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return WrapMapper.error("角色编码已存在");

    }

    /***
     * 删除
     */
    @DeleteMapping
    public Wrapper deleteById(Long id){
        try {
            Assert.notNull(id,"删除id不能为空");
            roleService.deleteById(id);
            return WrapMapper.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return WrapMapper.error("删除失败！");
        }
    }

    /***
     * 新增角色
     * @param entity
     * @return
     */
    @PostMapping
    public Wrapper<RoleType> add(@RequestBody RoleType entity){
        try{
        boolean codeExist = roleService.codeExists(entity.getCode());
        if(codeExist){
            return  WrapMapper.error("角色名称已存在！");
        }
            roleService.create(entity);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return WrapMapper.error("保存错误");
        }
        return WrapMapper.ok();
    }
}
