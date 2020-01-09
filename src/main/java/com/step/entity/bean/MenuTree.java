package com.step.entity.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhushubin  on 2019-11-04.
 * email:604580436@qq.com
 */
@Data
public class MenuTree extends TreeNode implements Serializable {
    private static final long serialVersionUID = 7900933523120251552L;

    private  boolean spread = false;
    private  String path;
    private  String component;
    private  String authority;
    private  String redirect;

    private  String label;
    public MenuTree(){}
    public MenuTree(int id, String name, int parentId) {
        this.id = id;
        this.parentId = parentId;
        this.title = name;
        this.label = name;
    }
    public MenuTree(int id, String name, MenuTree parent) {
        this.id = id;
        this.parentId = parent.getId();
        this.title = name;
        this.label = name;
    }



}
