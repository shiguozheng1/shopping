package com.step.entity.primary.form;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author zhushubin
 * @date 2019-08-29
 * email:604580436@qq.com
 * 每刻报销 预算占用请求
 */
@Data
public class FnaBudgetExecForm implements Serializable{
   private static final long serialVersionUID = -1294153346337268435L;
   @NotEmpty(message = "requestId cannot be null")
   private String requestId;
   @Valid // 嵌套验证必须用@Valid
   @NotNull(message = "budgetExecEntries cannot be null")
   @Size(min = 1, message = "budgetExecEntries must have one Item")
   private List<BudgetExecEntrie>  budgetExecEntries;


}
