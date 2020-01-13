package com.step.entity.secondary.shopping;
import com.step.entity.secondary.Admin;
import com.step.entity.secondary.shopping.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * shigz
 * 2020/1/9
 **/
@Entity
@Table(name = "f_area")
@Data
public class Area extends BaseEntity<Long>{
    /**
     * 树路径分隔符
     */
    public static final String TREE_PATH_SEPARATOR = ",";

    /**
     * 名称
     */
    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false)
    private String name;

    /**
     * 全称
     */
    @Column(nullable = false, length = 4000)
    private String fullName;

    /**
     * 树路径
     */
    @Column(nullable = false)
    private String treePath;

    /**
     * 层级
     */
    @Column(nullable = false)
    private Integer grade;

    /**
     * 上级地区
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Area parent;


    /**
     * 下级地区
     */
    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Area> children;

    @Transient
    public Long[] getParentIds(){
        String[] parents=StringUtils.split(getTreePath(),TREE_PATH_SEPARATOR);
        Long[] result = new Long[parents.length];
        for(int i=0;i<parents.length;i++){
            result[i]= Long.valueOf(parents[i]);
        }
        return result;
    }

    /**
     * 获取所有上级地区
     *
     * @return 所有上级地区
     */
    @Transient
    public List<Area> getParents() {
        List<Area> parents = new ArrayList<>();
        recursiveParents(parents, this);
        return parents;
    }

    private void recursiveParents(List<Area> parentList,Area area){
        if(area==null){
            return;
        }
        Area parent = area.getParent();
        if(parent!= null){
            parentList.add(0,parent);
            recursiveParents(parentList,parent);
        }
    }


    @Override
    public String toString() {
        return "Area{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", treePath='" + treePath + '\'' +
                ", grade=" + grade +
                '}';
    }
}
