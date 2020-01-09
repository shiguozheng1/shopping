package com.step.entity.secondary;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
public class JobDetailKey implements Serializable {
    private static final long serialVersionUID = -713224277143361855L;
    @Column(name="JOB_NAME")
    private String jobName;
    @Column(name="JOB_GROUP")
    private String jobGroup;
    @Column(name="JOB_CLASS_NAME")
    private String jobClassName;
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(jobName)
                .append(jobGroup)
                .toHashCode();
    }
}
