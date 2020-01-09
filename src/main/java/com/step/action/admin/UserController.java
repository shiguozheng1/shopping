package com.step.action.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.step.entity.primary.HrmDepartment;
import com.step.entity.secondary.*;
import com.step.entity.secondary.dto.AdminDto;
import com.step.entity.secondary.form.UserForm;
import com.step.entity.secondary.vo.AdminVo;
import com.step.entity.secondary.vo.PermissionInfoVo;
import com.step.service.admin.PermissionService;
import com.step.service.admin.RoleService;
import com.step.service.admin.UserService;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-10-30.
 * email:604580436@qq.com
 */
@RestController("adminUserController")
@RequestMapping("/admin/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 个人信息修改
     * @param dto
     * @return
     */
   @PutMapping("/info")
    public Wrapper update(@RequestBody AdminDto dto){
        Assert.notNull(dto.getUsername(),"账号名不能为空");
        try{
            Admin dbUser = userService.findByUserName(dto.getUsername());
            BeanUtils.copyProperties(dto,dbUser);
            dbUser.setSex(dto.getSex());
            dbUser.setRoles(dbUser.getRoles());
            userService.update(dbUser);
            return WrapMapper.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return WrapMapper.error(e.getMessage());
        }
    }
    @GetMapping("getUser")
    public Wrapper getUser(String username) {
        if(StringUtils.isEmpty(username)){
            throw new IllegalArgumentException("用户名不允许为空");
        }
        Admin admin = userService.findByUserName(username);
        List<Long> roleIds = admin.getRoles().stream().map(e -> {
            return e.getId();
        }).collect(Collectors.toList());
        List<ResourceAuthority> authorities=null;
        if(roleIds!=null&&roleIds.size()>0) {
            authorities = permissionService.findByRoleIds(roleIds);
        }
        List<PermissionInfoVo> menus = Lists.newArrayList();
        List<PermissionInfoVo> elements = Lists.newArrayList();
        Function<ResourceAuthority,PermissionInfoVo> converter = new Function<ResourceAuthority, PermissionInfoVo>() {
            @Override
            public PermissionInfoVo apply(ResourceAuthority authority) {
                PermissionInfoVo vo = new PermissionInfoVo();
                if(authority.getResourceType().equals(ResourceAuthority.ResourceType.menu)){
                    Menu menu = authority.getMenu();
                    if (StringUtils.isBlank(menu.getHref())) {
                        menu.setHref("/" + menu.getCode());
                    }
                    vo.setId(menu.getId());
                    vo.setCode(menu.getCode());
                    vo.setType(ResourceAuthority.ResourceType.menu.name());
                    vo.setName(ResourceAuthority.RESOURCE_ACTION_VISIT);
                    String uri = menu.getHref();
                    if (!uri.startsWith("/")) {
                        uri = "/" + uri;
                    }
                    vo.setUri(uri);
                    vo.setMethod("get");
                    vo.setMenu(menu.getTitle());
                }else{
                    Element element= authority.getElement();
                    vo.setId(element.getId());
                    vo.setCode(element.getCode());
                    vo.setType(element.getType().name());
                    vo.setUri(element.getUri());
                    vo.setMethod(element.getMethod());
                    vo.setName(element.getName());
                    vo.setMenu(element.getMenuId()+"");
                }
                return vo;
            }
        };
        if(authorities!=null&&authorities.size()>0){
            authorities.stream().forEach(e->{
                if(e.getResourceType().equals(ResourceAuthority.ResourceType.menu)){
                    menus.add(converter.apply(e));
                }else{
                    elements.add(converter.apply(e));
                }
            });
        }
        AdminVo adminVo = new AdminVo();
        BeanUtils.copyProperties(admin, adminVo);
        adminVo.setMenus(menus);
        adminVo.setElements(elements);
        return WrapMapper.ok(adminVo);
    }

    /**
     * 检查编号是否存在
     */
    @GetMapping("/check_name")
    public Wrapper  checkCode(String username) {
        if(StringUtils.isEmpty(username)) {
          return   WrapMapper.error("用户名不能为空");
        }
        try{
            boolean  flag= userService.usernameExists(username);
            if(!flag){
                return WrapMapper.ok();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return WrapMapper.error("用户名已存在");
    }

    /**
     * 验证密码是否正确
     */
    @GetMapping("/check_password")
    public Wrapper  checkCode(String username, String password) {
        Assert.notNull(username,"账号名不能为空");
        try{
            Admin admin = userService.findByUserName(username);
            if(admin==null||!password.equals(admin.getEncodedPassword())){
                return WrapMapper.error();
            }
           return WrapMapper.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return WrapMapper.error(e.getMessage());
        }
    }


    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param admin
     * @return
     */
    @GetMapping("/page")
    public Wrapper queryByPage(@RequestParam(value = "pageNum")Integer pageNum, @RequestParam(value = "pageSize") Integer pageSize,
                               Admin admin
                                 ){
        PageRequest request = PageRequest.of(pageNum, pageSize, Sort.Direction.ASC,"id");
        try {
            ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains());
            Example<Admin> example = Example.of(admin, matcher);
            Page<Admin> result = userService.findByPage(example, request);
            Page<AdminVo> ret=  result.map(new Function<Admin, AdminVo>() {

                @Override
                public AdminVo apply(Admin admin) {
                    AdminVo adminVo = new AdminVo();
                    BeanUtils.copyProperties(admin,adminVo);
                    adminVo.setId(admin.getId());
                    return adminVo;
                }
            });
            return WrapMapper.ok(ret);
        }catch (Exception e){
            e.printStackTrace();
            return WrapMapper.error("人员信息获取异常");
        }

    }

    /**
     * 添加
     * @param userForm
     * @return
     */
    @PostMapping
    public Wrapper add(@RequestBody UserForm userForm){
        Assert.notNull(userForm,"保存人员信息不能为空");
        boolean exists = userService.usernameExists(userForm.getUsername());
        if(exists){
           return WrapMapper.error("人员已存在");
        }
            try{
                Admin admin =new Admin();
                BeanUtils.copyProperties(userForm,admin);
                if(userForm.getRoles()!=null){
                    List<Role> roles=roleService.findAddByIds(userForm.getRoles());
                    admin.setRoles(Sets.newHashSet(roles));
                }
                userService.create(admin);
                return WrapMapper.ok();
            }catch (Exception e){
                log.error(e.getMessage());
                return WrapMapper.error(e.getMessage());
            }
    }

    @GetMapping("findById")
    public Wrapper findById(Long id){
        Assert.notNull(id,"id不能为空");
        Admin admin = userService.findById(id);
        if(admin==null){
            return WrapMapper.error("用户不存在");
        }
        List<Long> roleIds = admin.getRoles().stream().map(e -> {
            return e.getId();
        }).collect(Collectors.toList());
        UserForm userForm = new UserForm();
        BeanUtils.copyProperties(admin,userForm);
        userForm.setId(admin.getId());
        userForm.setRoles(roleIds);
        return WrapMapper.ok(userForm);
    }

    /***
     * 管理员修改
     * @param entity
     * @return
     */
    @PutMapping
    public Wrapper<Admin> put(@RequestBody UserForm entity){
        Assert.notNull(entity,"参数错误");
        Assert.notNull(entity.getId(),"用户名id不能为空");
        Admin user = userService.findById(entity.getId());
        if(user==null){
            return WrapMapper.error("用户不存在");
        }
        Admin admin =new Admin();
        BeanUtils.copyProperties(entity,admin);
        try{
            List<Role> roles=roleService.findAddByIds(entity.getRoles());
            admin.setRoles(Sets.newHashSet(roles));
            entity.setPassword(null);
            admin.setId(user.getId());
            userService.update(admin);
        }catch (Exception e){
            log.error(e.getMessage());
            WrapMapper.error(e.getMessage());
        }
        return WrapMapper.ok();
    }

    /***
     * 修改密码
     * @return
     */
    @PutMapping("/password")
    public Wrapper<Admin> updatePassword(@RequestBody Admin entity){
        Admin admin = userService.findByUserName(entity.getUsername());
        try{
            admin.setEncodedPassword(entity.getEncodedPassword());
            userService.update(admin);
            return WrapMapper.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return WrapMapper.error(e.getMessage());
        }

    }

    /***
     * 删除
     */
    @DeleteMapping
    public Wrapper deleteById(Long id){
        try {
            Assert.notNull(id,"删除id不能为空");
            Admin admin = userService.findById(id);
            if(admin==null){
                return WrapMapper.error("用户不存在");
            }
            userService.delete(admin);
            return WrapMapper.ok();
        }catch (Exception e){
            log.error(e.getMessage());
            return WrapMapper.error("删除失败！");
        }
    }
}
