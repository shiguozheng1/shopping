package com.step.entity.secondary.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.step.entity.secondary.RoleType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhushubin  on 2019-11-08.
 * email:604580436@qq.com
 */
@Data
public class RoleTypeVo implements Serializable {

    private Long id;
    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     *修改人
     */
    private String modifiedUser;


    /**
     * 描述
     */
    private String description;

    /**
     * 是否内置
     */
    private Boolean isSystem ;


    /**
     * 创建日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdDate;

    /**
     * 最后修改日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifiedDate;
}
