package com.step.entity.primary;

import com.step.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

/**
 * shigz
 * 2019/11/18
 * 流程信息
 **/
@Data
public class ProcessInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name="PROCESS_NAME")
    private String processName;

    @Column(name="PROCESS_DESC")
    private String processDesc;

    @Column(name = "CREATE_USER_ID")
    private Integer createUserId;

    @CreatedDate
    @Column( name = "CREATE_TIME", nullable = false, updatable = false)
    private Date createdDate;

    @Column(name = "PROCESS_DEF_ID")
    private Integer processDefId;

    @Column(name = "PROCESS_XML")
    private String processXML;

    @Column(name = "PROCESS_TYPE_ID")
    private Integer processTypeId;

    @Column(name = "PROCESS_STATUS")
    private Integer state;

    @Column(name = "ACTORID_EXPRESSION")
    private Integer expression;


}
