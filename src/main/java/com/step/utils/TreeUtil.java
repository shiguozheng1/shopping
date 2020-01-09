package com.step.utils;

import com.google.common.collect.Lists;
import com.step.entity.bean.MenuTree;
import com.step.entity.bean.RoleTree;
import com.step.entity.bean.TreeNode;
import com.step.entity.secondary.vo.AntdTree;
import com.step.entity.secondary.vo.AntdTreeNode;
import org.springframework.core.convert.converter.Converter;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-11-04.
 * email:604580436@qq.com
 */
public class TreeUtil {
    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static <T extends TreeNode> List<T> bulid(List<T> treeNodes, Object root) {

        List<T> trees = new ArrayList<T>();

        for (T treeNode : treeNodes) {

            if (root.equals(treeNode.getParentId())|| treeNode.getParentId()== 0) {
                trees.add(treeNode);
            }

            for (T it : treeNodes) {
                if (it.getParentId() == treeNode.getId()) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<TreeNode>());
                    }
                    treeNode.add(it);
                }
            }
        }
        return trees;
    }





    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes,Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getId() == it.getParentId()) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<TreeNode>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }

    /***
     * 将treeNode 装换成 RoleTree
     * @param treeNodes 树节点列表
     * @param converter
     * @return
     */
    public static <T extends TreeNode,V extends AntdTreeNode> List<V> convertToAntTree(List<T> treeNodes, Converter<T, V> converter) {
         List<V> result = Lists.newArrayList();
         if(treeNodes!=null&&treeNodes.size()>0){
             V tmp=null;
             for(T node: treeNodes){
                 tmp = converter.convert(node);
                 if(tmp!=null){

                     if(node.getChildren()!=null&&node.getChildren().size()>0){
                         List<V>  nodes=  convertToAntTree((List<T>) node.getChildren(),converter);
                         if(nodes!=null&&nodes.size()>0) {
                             tmp.setChildren((List<AntdTreeNode>) nodes);
                         }
                     }
                     result.add(tmp);
                 }

             }

         }

        return result;
    }

    public static <T extends TreeNode,V extends AntdTreeNode> List<V> bulidAntTree(List<T> treeNodes, Object root, Converter<T, V> converter) {
        List<V> trees = new ArrayList<V>();
        Map<Integer,V> map =new ConcurrentHashMap<>();
        for(T trr:treeNodes){
            map.put(trr.getId(), converter.convert(trr));
        }

        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())|| treeNode.getParentId()== 0) {
                trees.add(map.get(treeNode.getId()));
            }
            for (T it : treeNodes) {
                if (it.getParentId() == treeNode.getId()) {
                    V parentTree = map.get(treeNode.getId());
                    if (parentTree.getChildren() == null) {
                        parentTree.setChildren(new ArrayList<AntdTreeNode>());
                    }
                    parentTree.add(map.get(it.getId()));
                }
            }
        }
        return trees;
    }
}
