package com.step.entity.primary;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by zhushubin  on 2019-09-16.
 * email:604580436@qq.com
 */
@Entity
@Table(name = "HrmRoles")
@Data
public class HrmRoles{
    @Id
    protected Long id;
    @Column(name="rolesmark")
   private String rolesMark;
    @Column(name="rolesname")
   private String rolesName;
}
