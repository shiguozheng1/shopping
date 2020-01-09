package com.step.entity.secondary.vo;

import com.step.entity.bean.TreeNode;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhushubin  on 2019-11-04.
 * email:604580436@qq.com
 */
@Data
public class AntdTreeNode {
    private String key;
    private String title;
    private String icon;
    private String code;
    protected List<AntdTreeNode> children = new ArrayList<AntdTreeNode>();
    /***
     * 是否被选中
     */
    private boolean selectable = true;
    @Transient
    public void add(AntdTreeNode node) {
        this.children.add(node);
    }
}
