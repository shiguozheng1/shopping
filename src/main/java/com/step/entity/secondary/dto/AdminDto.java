package com.step.entity.secondary.dto;

import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author zhushubin
 * @date 2019-10-30
 * email:604580436@qq.com
 */
@Data
public class AdminDto implements Serializable{

   private String  avatar;
   private Date   birth;
   private Integer sex;
   private String location;
   private String phone;
   private String username;
}
