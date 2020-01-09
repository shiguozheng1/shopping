package com.step.action.admin;

import com.alibaba.druid.sql.visitor.functions.Now;
import com.fasterxml.jackson.annotation.JsonView;
import com.step.action.BaseController;
import com.step.entity.bean.MenuTree;
import com.step.entity.secondary.Menu;
import com.step.entity.secondary.vo.AntdTree;
import com.step.service.admin.MenuService;
import com.step.utils.TreeUtil;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.persistence.AttributeConverter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhushubin  on 2019-11-04.
 * email:604580436@qq.com
 * 菜单-controller
 */
@RestController("adminMenuController")
@RequestMapping("/admin/menu")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;
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
            Menu tmpMenu= menuService.findByCode(code);
            if(tmpMenu!=null){
                if(!tmpMenu.getId().equals(id)){
                    return  WrapMapper.error("路径编码已被占用！");
                }
            }
        }else{
             flag= !menuService.codeExists(code);
        }
        if(flag){
            return WrapMapper.ok();
        }
        return WrapMapper.error("路径编码已存在");
    }

    @GetMapping
    @JsonView(Menu.BaseMenuView.class)
    public Wrapper get(Long id){
        Assert.notNull(id,"id 不允许为空");
        Menu menu = menuService.find(id);
        if(menu==null){
            return WrapMapper.error("无法获取到菜单信息");
        }
        return WrapMapper.ok(menu);
    }

    /***
     * 新增菜单项
     * @param entity
     * @return
     */
    @PostMapping
    public Wrapper<Menu> add(@RequestBody Menu entity){
        boolean codeExist = menuService.codeExists(entity.getCode());
        if(codeExist){
            return  WrapMapper.error("路径编码已存在！");
        }
        if(entity.getParentId()!=null) {
            entity.setParent(menuService.find(entity.getParentId()));
        }
        Menu menu =  menuService.create(entity);
        return WrapMapper.ok();
    }



    /***
     * 新增菜单项
     * @param entity
     * @return
     */
    @PutMapping
    public Wrapper<Menu> put(@RequestBody Menu entity){
        Assert.notNull(entity.getCode());
        Menu tmpMenu= menuService.findByCode(entity.getCode());
        if(tmpMenu!=null){
            if(!tmpMenu.getId().equals(entity.getId())){
                return  WrapMapper.error("路径编码已被占用！");
            }
        }
        if(entity.getParentId()!=null) {
            entity.setParent(menuService.find(entity.getParentId()));
        }
        Menu menu =  menuService.update(entity);
        return WrapMapper.ok();
    }

    /***
     * 删除
     * @param id id
     */
    @DeleteMapping
    public Wrapper<Menu> del(Long id){
        Assert.notNull(id);
        try {
            menuService.delete(id);
            return WrapMapper.ok();
        }catch (Exception e){
            return WrapMapper.error("删除失败！");
        }

    }

    @GetMapping(value = "/tree")
    public Wrapper getTree(String title) {
        List<Menu> menus=menuService.findAll();
        List<AntdTree> ret =getMenuTree(menus,-1);
        return WrapMapper.ok(ret);
    }
    protected List<AntdTree> getMenuTree(List<Menu> menus, int root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (Menu menu : menus) {
            node = new MenuTree();
            BeanUtils.copyProperties(menu, node);
            node.setId(menu.getId().intValue());
            node.setIcon(menu.getIcon());
            node.setLabel(menu.getTitle());
            node.setCode(menu.getCode());
            if(menu.getParent()!=null){
                node.setParentId(menu.getParent().getId().intValue());
            }else{
                node.setParentId(-1);
            }

            trees.add(node);
        }
        Converter<MenuTree,AntdTree> converter = new Converter<MenuTree,AntdTree>() {
            @Override
            public AntdTree convert(MenuTree menuTree) {
                AntdTree antdTree = new AntdTree();
                antdTree.setKey( menuTree.getId()+"");
                antdTree.setTitle(menuTree.getTitle());
                antdTree.setIcon(menuTree.getIcon());
                antdTree.setCode(menuTree.getCode());
                return antdTree;
            }
        };
        List<AntdTree> antdTreeList=   TreeUtil.bulidAntTree(trees,root,converter);
        return antdTreeList;
    }
}
