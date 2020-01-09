package com.step.properties;

import com.step.utils.Sha256Util;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * @Author: shiguozheng
 * @Date: 2019/8/28
 **/
@Component
@ConfigurationProperties(prefix = "maycur")
@Data
public class MaycurProperties {
    private String hostUrl;
    private String appCode;
    private String appPwd;
    private Long companyId;
    private String stepBusinessCode;
    private List<String> administrator;
    public String  getLoginUrl(){
        return String.format(hostUrl,"auth/login");
    }
    public String getSubDataUrl(){
        return String.format(hostUrl,"org/subsidiaries");
    }
    public String getSaveSubUrl(){
        return String.format(hostUrl,"org/subsidiary/save");
    }
    public String getDeleteSubUrl(){
        return String.format(hostUrl,"org/subsidiary/delete");
    }
    public String getDeptDataUrl(){
        return String.format(hostUrl,"org/departments");
    }
    public String getCostTrackingSaveUrl(){
       return String.format(hostUrl,"/costtracking/item/save");
    }
    public String getSaveDeptUrl(){
        return String.format(hostUrl,"org/department/save");
    }
    public String getUpdateDeptUrl(){
        return String.format(hostUrl,"org/department/part/update");
    }
    public String getDeleteDeptUrl(){
        return String.format(hostUrl,"org/department/delete");
    }
    public String getFindEmployeeUrl(){
        return String.format(hostUrl,"employee");
    };
    public String getSaveEmployeeUrl(){
        return String.format(hostUrl,"employee/save");
    }
    public String getUpdateEmployeeUrl(){
        return String.format(hostUrl,"employee/part/update");
    }
    public String getSaveEmpDepartUrl(){
        return String.format(hostUrl,"employee/department/save");
    }
    public String getDeleterEmployeeUrl(){
        return String.format(hostUrl,"employee/department/delete");
    }
    public String getFindCosttrackUrl(){
        return String.format(hostUrl,"costtracking/item/getCostTrackingItem");
    }
    public String getSaveCosttrackUrl(){
        return String.format(hostUrl,"costtracking/item/save");
    }
    public String getDeleteCosttrackUrl(){
        return String.format(hostUrl,"costtracking/item/delete");
    }
    public String getAuthCosttrackUrl(){
        return String.format(hostUrl,"costtracking/item/auth");
    }
    public String getRevokeCosttrackUrl(){
        return String.format(hostUrl,"costtracking/item/revoke");
    }
    public String getDisableEmployeeUrl(){
        return String.format(hostUrl,"employee/disable");
    }
    public String getDeleteEmpDepUrl(){
        return String.format(hostUrl,"employee/department/delete");
    }

    /***
     * 新增更新用户组url
     * @return
     */
    public String getPostUserGroupUrl(){
        return String.format(hostUrl,"/usergroup/save");
    }
    public String getSecret(long timestamp){
        //appPwd + appCode

        String secret = Sha256Util.getSHA256StrJava(appPwd+":"+appCode+":"+timestamp);
        return secret;
    }

}
