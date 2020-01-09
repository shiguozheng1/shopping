package com.step.entity.primary.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.step.entity.primary.form.BudgetExecEntrie;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhushubin  on 2019-08-30.
 * email:604580436@qq.com
 */
@Setter
public class FnaBudgetExecResult {
    @JsonProperty(value = "isSuccess")
    private boolean isSuccess;
    @JsonProperty(value = "budgetExecEntries")
    private List<BudgetExecEntrie> budgetExecEntries;
    @JsonIgnore
    public boolean isSuccess() {
        return isSuccess;
    }

    public List<BudgetExecEntrie> getBudgetExecEntries() {
        return budgetExecEntries;
    }
}
