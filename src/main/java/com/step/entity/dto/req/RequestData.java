package com.step.entity.dto.req;

import com.step.entity.dto.MaycurCompany;
import com.step.entity.dto.req.BaseRequestData;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * shigz
 * 2019/8/30
 **/
@Data
public class RequestData extends BaseRequestData {
    private Long timestamp;
    public RequestData(Object data) {
        this.data = data;
        this.timestamp= Instant.now().toEpochMilli();
    }
}
