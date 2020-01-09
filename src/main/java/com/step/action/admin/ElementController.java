package com.step.action.admin;

import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.secondary.Element;
import com.step.entity.secondary.Menu;
import com.step.entity.secondary.vo.ElementVo;
import com.step.service.admin.ElementService;
import com.step.service.admin.MenuService;
import com.step.utils.wrapper.WrapMapper;
import com.step.utils.wrapper.Wrapper;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by zhushubin  on 2019-11-06.
 * email:604580436@qq.com
 * 按钮或资源-controller
 */
@RestController("adminElementController")
@RequestMapping("/admin/element")
@Slf4j
public class ElementController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private ElementService elementService;

    /***
     * 新增资源或按钮
     * @param entity 按钮或资源
     * @return json
     */
    @PostMapping
    @JsonView(Element.ElementView.class)
    public Wrapper add(@RequestBody Element entity){
        Menu menu=null;
        if(entity.getMenuId()!=null){
            menu=menuService.find(entity.getMenuId());
        }
        entity.setMenu(menu);
        boolean codeExist= elementService.codeExists(entity.getCode());
        if(codeExist){
            return WrapMapper.error("资源编码已存在！");
        }
        Element element = elementService.save(entity);
        return WrapMapper.ok(element);
    }
    /***
     * 更新资源或按钮
     * @param entity 按钮或资源
     * @return json
     */
    @PutMapping
    @JsonView(Element.ElementView.class)
    public Wrapper put(@RequestBody Element entity){
        Menu menu=null;
        Assert.notNull(entity);
        Assert.notNull(entity.getId());
        Assert.notNull(entity.getMenuId());
        if(entity.getMenuId()!=null){
            menu=menuService.find(entity.getMenuId());
        }
        entity.setMenu(menu);
        Element bak= elementService.findByCode(entity.getCode());
        if(bak!=null&&!bak.getId().equals(entity.getId())){
            return WrapMapper.error("资源编码被占用！");
        }
        Element element = elementService.save(entity);
        return WrapMapper.ok(element);
    }


    @GetMapping(value = "/list")
    public Wrapper page(@RequestParam(defaultValue = "10") int limit,
                              @RequestParam(defaultValue = "1") int offset,
                              String name,
                              @RequestParam(defaultValue = "0") Long menuId) {
        PageRequest pageRequest = PageRequest.of(0,10);
        Element element = new Element();
        element.setName(name);
        element.setMenuId(menuId);

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name"
                ,ExampleMatcher.GenericPropertyMatchers.endsWith())//endsWith是categoryName 结尾为喜欢的数据
                .withMatcher("name",ExampleMatcher.GenericPropertyMatchers.startsWith()) ;  //

        Example<Element> example = Example.of(element,matcher);
        Page<Element> page= elementService.findPage(example,pageRequest);
        Function<Element,ElementVo> converter = new Function<Element, ElementVo>() {
            @Override
            public ElementVo apply(Element element) {
                    ElementVo vo  = new ElementVo();
                    BeanUtils.copyProperties(element,vo);
                    vo.setId(element.getId());
                    return vo;
            }
        };
        Page<ElementVo> ret = page.map(converter);
        return WrapMapper.ok(ret);
    }

    /***
     * 获取按钮或资源
     * @param id 主键
      * @return
     */
    @GetMapping
    @JsonView(Element.ElementView.class)
    public Wrapper get(Long id){
        Assert.notNull(id);
        Element element =  elementService.find(id);
        if(element==null){
            return WrapMapper.error("无法找到此资源");
        }
        return WrapMapper.ok(element);
    }
    @DeleteMapping
    public Wrapper delete(Long id){
        Assert.notNull(id);
        try {
            elementService.delete(id);
        }catch (Exception e){
             log.error(e.getMessage());
            return WrapMapper.error("删除失败");
        }
        return WrapMapper.ok();
    }
}
