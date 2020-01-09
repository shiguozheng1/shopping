package com.step.entity.secondary.vo;

import com.step.entity.secondary.Element;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * Created by zhushubin  on 2019-11-06.
 * email:604580436@qq.com
 * 按钮或资源vo
 */
@Data
public class ElementVo implements Serializable{
    private Long id;
    /***
     * 编码
     */
    private String code;
    /***
     * 类型
     */
    @Enumerated(EnumType.STRING)
    private Element.ElementType type;
    /***
     * 名称
     */
    private String name;
    /***
     * 链接地址
     */
    private String uri;
    private String path;
    private String method;
    private String description;
}
