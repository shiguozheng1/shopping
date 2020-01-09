package com.step.entity.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * shigz
 * 2019/9/3
 **/
@Data
public class ResponseSearchBudget {
    @JsonProperty(value = "isSuccess")
    private Boolean isSuccess =false;
    @JsonProperty(value = "budgetExecEntries")
    private Object budgetExecEntries;
    public ResponseSearchBudget(){}
    public ResponseSearchBudget(Object data) {
        this.budgetExecEntries = data;
    }
}
