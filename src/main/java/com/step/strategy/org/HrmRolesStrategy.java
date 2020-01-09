package com.step.strategy.org;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.step.entity.dto.req.BaseRequestData;
import com.step.entity.dto.res.BaseResponse;
import com.step.entity.dto.res.ResponsePostData;
import com.step.entity.dto.res.UserGroupResponse;
import com.step.entity.primary.HrmRoles;
import com.step.exception.OrgException;
import com.step.properties.MaycurProperties;
import com.step.service.HrmResourceService;
import com.step.service.HrmRolesService;
import com.step.service.MaycurService;
import com.step.service.RestService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-09-16.
 * email:604580436@qq.com
 * 用户组同步，对应OA 角色
 */
@Component("role")
public class HrmRolesStrategy extends Strategy{
    @Autowired
    private HrmRolesService hrmRolesService;

    @Override
    public void invoke() throws OrgException {
        StringBuilder builder = new StringBuilder();
        try{
        List<HrmRoles> hrmRolesList=  hrmRolesService.findByRolesNameLikeOrderByRolesNameAsc("众为兴差补%");
        Assert.notEmpty(hrmRolesList,"此角色组不存在");
        List<UserGroup> userGroups =hrmRolesList.stream().map(e->{
            UserGroup userGroup = new UserGroup(e.getId(),e.getRolesName());
            SubUserGroup subUserGroup = new SubUserGroup();
            subUserGroup.setName(e.getRolesName());
            subUserGroup.setBusinessCode(e.getId().toString());
            List<String> memberIds =hrmRolesService.findMemberByRoleId(e.getId());
            if(memberIds!=null){
                subUserGroup.setAssigneeEmployeeIds(memberIds);
            }
            userGroup.setSubUserGroups(Lists.newArrayList(subUserGroup));
            userGroup.setDefaultSubUserGroup(subUserGroup);
            return userGroup;
        }).collect(Collectors.toList());
        userGroups.forEach(userGroup->{
            BaseRequestData baseRequestData = new BaseRequestData();
            baseRequestData.setData(Lists.newArrayList(userGroup));
            //批量保存数据
            ResponsePostData<List<UserGroupResponse>> ret=  restService.requestSave(token.getTokenId(), token.getEntCode(), baseRequestData, maycurProperties.getPostUserGroupUrl(), new TypeReference<ResponsePostData<List<UserGroupResponse>>>(){});
            if(ret!=null&&!ret.getCode().equals("ACK")){
                builder.append(ret.getMessage());
            }
        });
        }catch (Exception e){
            e.printStackTrace();
            builder.append(e.getMessage());
        }
       if(StringUtils.isNotEmpty(builder.toString())){
           throw new OrgException(builder.toString());
       }
    }


    /***
     * 用户组
     */
    public class UserGroup implements Serializable{
        private String name;
        private String businessCode;
        private List<SubUserGroup> subUserGroups;
        private SubUserGroup defaultSubUserGroup;
        public UserGroup(){}
        public UserGroup(Long id, String rolesName) {
            this.businessCode = id.toString();
            this.name = rolesName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBusinessCode() {
            return businessCode;
        }

        public void setBusinessCode(String businessCode) {
            this.businessCode = businessCode;
        }

        public List<SubUserGroup> getSubUserGroups() {
            return subUserGroups;
        }

        public void setSubUserGroups(List<SubUserGroup> subUserGroups) {
            this.subUserGroups = subUserGroups;
        }

        public SubUserGroup getDefaultSubUserGroup() {
            return defaultSubUserGroup;
        }

        public void setDefaultSubUserGroup(SubUserGroup defaultSubUserGroup) {
            this.defaultSubUserGroup = defaultSubUserGroup;
        }
    }

    /***
     * 子用户组
     */
    public class SubUserGroup{
        private String name;
        private String businessCode;
        private List<String> assigneeEmployeeIds;
        private String condition;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBusinessCode() {
            return businessCode;
        }

        public void setBusinessCode(String businessCode) {
            this.businessCode = businessCode;
        }

        public List<String> getAssigneeEmployeeIds() {
            return assigneeEmployeeIds;
        }

        public void setAssigneeEmployeeIds(List<String> assigneeEmployeeIds) {
            this.assigneeEmployeeIds = assigneeEmployeeIds;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }

}
