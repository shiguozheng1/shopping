package com.step.entity.primary.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author zhushubin
 * @date 2019-08-30
 * email:604580436@qq.com
 */
@Data
public class BudgetExecEntrie implements Serializable,Cloneable {
    private static final long serialVersionUID = 2564280270349531292L;
    /***
     * 预算编码
     */
    @NotBlank(message = "budgetCode cannot be null")
    private String budgetCode;
    /***
     * 业务编码
     */
    @NotBlank(message = "bizCode cannot be null")
    private String bizCode;
    /***
     * 预算执行的预算金额
     */
    private String amount;
    /***
     * 操作
     */
    @NotBlank(message = "opt cannot be null")
    private String opt;
    private String errMsgZh;
    private String errMsgEn;
    /***
     * 金额字段
     */
    private BigDecimal decAmount;
    /***
     * 单据编号
     */
    private String formCode;

    public BigDecimal getDecAmount() {
        if(decAmount ==null){
           return BigDecimal.ZERO;
        }
        return decAmount;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object o=null;
        try
        {
            o=(BudgetExecEntrie)super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。
        }
        catch(CloneNotSupportedException e)
        {
            System.out.println(e.toString());
        }
        return o;
    }

    /***
     * 判断是否为特殊单据
     * @return
     */
    @Transient
    @JsonIgnoreProperties
    public boolean isSpecial(){
       if(StringUtils.isEmpty(formCode)){
           return false;
       }
       if(formCode.startsWith("ctrip_")){
           return true;
       }

       return false;
    }
    @Transient
    public String getKey(){
        return getBudgetCode()+getOpt();
    }
}
