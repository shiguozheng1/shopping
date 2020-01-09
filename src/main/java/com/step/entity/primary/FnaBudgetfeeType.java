package com.step.entity.primary;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author zhushubin
 * @date 2019-10-30
 * email:604580436@qq.com
 * 费用科目
 */
@Entity
@Table(name = "FnaBudgetfeeType")
@Data
public class FnaBudgetfeeType  {
    @Id
    private Long  id;
    private static final long serialVersionUID = 5485010414996927056L;
    /***
     *名称
     */
    private String name;
    /***
     * 是否启用
     */
    @Column(name="Archive")
    private Integer  archive;
    /***
     * 上级科目
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "supsubject")
    @NotFound(action = NotFoundAction.IGNORE)
    private FnaBudgetfeeType parent;
    /**
     * 下级科目
     */
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<FnaBudgetfeeType> children = new HashSet<>();
}
