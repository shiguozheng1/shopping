package com.step.entity.primary.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhushubin  on 2019-10-30.
 * email:604580436@qq.com
 */
@Data
public class FnaBudgetfeeTypeVo implements Serializable {
    private Long id;
    @JsonProperty("pId")
    private Long pId;
    private String value;
    private String title;
    @JsonProperty("disabled")
    private boolean disabled =false;
    @JsonProperty("selectable")
    private boolean selectable = true;
    @JsonProperty("disableCheckbox")
    private boolean disableCheckbox =false;
    @JsonProperty("isLeaf")
    private boolean isLeaf=false;
}
