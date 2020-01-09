package com.step.action.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.bean.MenuTree;
import com.step.entity.bean.RoleTree;
import com.step.entity.secondary.Menu;
import com.step.entity.secondary.Role;
import com.step.entity.secondary.RoleType;
import com.step.entity.secondary.form.PermissionForm;
import com.step.entity.secondary.vo.AntdTree;
import com.step.service.admin.RoleService;
import com.step.service.admin.RoleTypeService;
import com.step.utils.TreeUtil;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * shigz
 * 2019/11/8
 * 角色controller
 **/
@RestController("adminRoleController")
@RequestMapping("/admin/role")
@Slf4j
public class RoleController {
    @Autowired
    private RoleTypeService roleTypeService;
    @Autowired
    private RoleService roleService;


    /**
     * 检查编号是否存在
     */
    @GetMapping("/check_code")
    public Wrapper  checkSn(String code,Long id) {
        boolean flag=false;
        if(StringUtils.isEmpty(code)){
            WrapMapper.error("路径编码不允许为空");
        }
        if(id!=null){
            Role tmpRole= roleService.findByCode(code);
            if(tmpRole!=null){
                if(!tmpRole.getId().equals(id)){
                    return  WrapMapper.error("路径编码已被占用！");
                }
            }
        }else{
            flag= !roleService.codeExists(code);
        }
        if(flag){
            return WrapMapper.ok();
        }
        return WrapMapper.error("路径编码已存在");
    }


    /***
     * 新增角色
     * @param entity
     * @return
     */
    @PostMapping
    public Wrapper<Role> add( @RequestBody Role entity){
        Assert.notNull(entity,"参数错误");
        try{
            boolean codeExist = roleService.codeExists(entity.getCode());
            if(codeExist){
                return  WrapMapper.error("路径编码已存在！");
            }
            if(entity.getParentId()!=-1) {
                entity.setParent(roleService.find(entity.getParentId()));
            }
                entity.setRoleType(roleTypeService.findById(entity.getRoleTypeId()));
            roleService.create(entity);
            return WrapMapper.ok();
        }catch (Exception e){
            e.printStackTrace();
            return WrapMapper.error(e.getMessage());
        }
    }

    /***
     * 修改角色
     * @param entity
     * @return
     */
    @PutMapping
    public Wrapper<Role> put(@RequestBody Role entity){
        Assert.notNull(entity,"参数错误");
        Assert.notNull(entity.getCode(),"code is required");
        Role tmpRole= roleService.findByCode(entity.getCode());
        if(tmpRole!=null){
            if(!tmpRole.getId().equals(entity.getId())){
                return  WrapMapper.error("路径编码已被占用！");
            }
        }
        if(entity.getParentId()!=null) {
            entity.setParent(roleService.find(entity.getParentId()));
        }
        Role role =  roleService.update(entity);
        return WrapMapper.ok();
    }


    /***
     * 删除
     * @param id id
     */
    @DeleteMapping
    public Wrapper<Role> del(Long id){
        Assert.notNull(id,"id不能为空");
        try {
            roleService.delete(id);
            return WrapMapper.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return WrapMapper.error(e.getMessage());
        }

    }


    @GetMapping
    @JsonView(Role.BaseRoleView.class)
    public Wrapper get(Long id){
        Assert.notNull(id,"id 不允许为空");
        Role role = roleService.find(id);
        if(role==null){
            return WrapMapper.error("无法获取到菜单信息");
        }
        return WrapMapper.ok(role);
    }

    /***
     * 获取角色tree
     * @param code 角色类型
     * @return json
     */
    @RequestMapping("/tree")
    public Wrapper getTree(String code) {
        try{
            RoleType roleType = roleTypeService.findByCode(code);
            Set<Role> rootRoles= roleType.getRoles();
            Specification<Role> spc= new Specification<Role>() {
                @Override
                public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                    Predicate predicateOr= cb.or();
                    for(Role rootRole:rootRoles){
                        predicateOr.getExpressions()
                                .add(cb.or(cb.equal(root.get("id"),rootRole.getId())));
                        predicateOr.getExpressions().add(cb.like(root.get("treePath"),"%"+rootRole.getId()+"%"));
                    }
                   return cb.and(predicateOr);
                }
            };
            List<Role> roles =roleService.findAll(spc);
            List<AntdTree> ret = getRoleTree(roles, -1);
            return WrapMapper.ok(ret);
        }catch (Exception e){
            e.printStackTrace();
            return WrapMapper.error(e.getMessage());
        }
    }

    /***
     * 将角色list 转成树形图
     * @param roles
     * @param root
     * @return
     */
    protected List<AntdTree> getRoleTree(List<Role> roles, int root) {

        List<RoleTree> trees = new ArrayList<RoleTree>();
        RoleTree node = null;
        for (Role role : roles) {
            node = new RoleTree();
            BeanUtils.copyProperties(role, node);
            node.setId(role.getId().intValue());
            if(role.getParent()!=null){
                node.setParentId(role.getParent().getId().intValue());
            }else{
                node.setParentId(-1);
            }
            trees.add(node);
        }
        Converter<RoleTree,AntdTree> converter = new Converter<RoleTree,AntdTree>() {


            @Override
            public AntdTree convert(RoleTree roleTree) {
                AntdTree antdTree = new AntdTree();
                antdTree.setKey( roleTree.getId()+"");
                antdTree.setTitle(roleTree.getName());
                antdTree.setCode(roleTree.getCode());
                return antdTree;
            }
        };
        List<AntdTree> ret=  TreeUtil.bulidAntTree(trees,root,converter) ;
        return ret;
    }
}
