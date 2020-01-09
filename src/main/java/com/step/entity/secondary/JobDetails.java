package com.step.entity.secondary;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Table(name="QRTZ_JOB_DETAILS")
@Data
public class JobDetails {
    @EmbeddedId
   private JobDetailKey jobDetailKey;
}
