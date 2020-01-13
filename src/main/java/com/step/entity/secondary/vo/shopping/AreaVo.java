package com.step.entity.secondary.vo.shopping;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.step.entity.secondary.shopping.Area;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * shigz
 * 2020/1/9
 **/
@Data
public class AreaVo {

    private Long id;

    private Integer version;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastModifyDate;

    private String name;

    private String fullName;


    private String treePath;


    private Integer grade;

}
