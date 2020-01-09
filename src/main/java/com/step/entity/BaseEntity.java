package com.step.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.step.utils.wrapper.Wrapper;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author zhushubin
 * @date 2019-09-16
 * email:604580436@qq.com
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
public class BaseEntity<ID extends Serializable>  implements Serializable{
    private static final long serialVersionUID = 477486738955565567L;
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @JsonView(Wrapper.WrapperView.class)
    protected ID id;
    /**
     * 创建日期
     */

    @CreatedDate
    @Column(nullable = false, updatable = false,name = "created_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdDate;

    /**
     * 最后修改日期
     */
    @LastModifiedDate
    @Column(nullable = false,name = "modified_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifiedDate;

}
