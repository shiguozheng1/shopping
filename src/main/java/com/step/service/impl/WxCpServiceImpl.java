package com.step.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.step.config.Env;
import com.step.config.WxCpConfiguration;
import com.step.entity.primary.HrmResource;
import com.step.repository.primary.HrmDepartmentRepository;
import com.step.service.HrmResourceService;
import com.step.service.WxCpService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpUserService;
import me.chanjar.weixin.cp.bean.Gender;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-06-27.
 * email:604580436@qq.com
 */
@Service
@Slf4j
public class WxCpServiceImpl implements WxCpService {
    private me.chanjar.weixin.cp.api.WxCpService wxCpService;
    @Autowired
    private HrmDepartmentRepository hrmDepartmentRepository;
    @Autowired
    private HrmResourceService hrmResourceService;
    @PostConstruct
    public void init() {
        wxCpService = WxCpConfiguration.getCpService(1000001);
    }


    /***
     * 修改用户信息
     */
    @Override
    public void modifyUsers() {
        WxCpUserService wxCpUserService =  WxCpConfiguration.getCpService(1000001).getUserService();
        List<WxCpUser> wxCpUsers = null;
        try {
            wxCpUsers = wxCpUserService.listByDepartment(1L, true, 0);
        } catch (WxErrorException e) {
        e.printStackTrace();
    }
        //key:userId   value:depId
        Map<String,Long> map = new ConcurrentHashMap<>();
        List<HrmResource> dbUserList = hrmResourceService.findAll();
        dbUserList.forEach(dbUser -> {
              if(dbUser.getHrmDepartment()!=null) {
                     map.put(dbUser.getLoginid().trim(), dbUser.getHrmDepartment().getId());
              }

        });
        List<WxCpUser> updateUserList= Lists.newArrayList();
        List<WxCpUser> createUserList= Lists.newArrayList();
        wxCpUsers.forEach(user -> {
                    WxCpUser wxCpUser = new WxCpUser();
                    BeanUtils.copyProperties(user, wxCpUser);
                            //修改部门路径
                            Long depId = map.get(user.getUserId()+"");
                            if(depId  != null){
                                 boolean flag = false;
                                 for(Long tmpDepId :user.getDepartIds()){
                                     if(tmpDepId.equals(depId)){
                                         flag = true;
                                         break;
                                     }
                                 }

                                if (!flag) {
                                    wxCpUser.setDepartIds(new Long[]{depId});
                                    updateUserList.add(wxCpUser);
                                }
                            }else{
                                log.error("不匹配人员:{loginid},{中文名字},{部门}",wxCpUser.getUserId(),wxCpUser.getName(),wxCpUser.getDepartIds());
                            }
                }

        );



        if(!updateUserList.isEmpty()){
            updateUserList.forEach(modifyDepart->{
                try {
                    wxCpUserService.update(modifyDepart);
                }catch (WxErrorException e){
                    e.getStackTrace();
                }
            });
        }
    }

    //创建人员
    public void createUser(){
        WxCpUserService wxCpUserService =  WxCpConfiguration.getCpService(1000001).getUserService();
        List<WxCpUser> wxCpUserList = null;
        try {
            wxCpUserList = wxCpUserService.listByDepartment(1L, true, 0);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        List<HrmResource> dbUserList = hrmResourceService.findAll();
        //需要创建人员列表
        List<WxCpUser> userCreate=Lists.newArrayList();
        WxCpUser wxCpUser;
        for(HrmResource dbUser:dbUserList){
            boolean flag = false;
            for(WxCpUser wxUser:wxCpUserList){
                if(dbUser.getLoginid().equals(wxUser.getUserId())){
                   flag=true;
                   break;
                }
            }
            if(!flag){//未找到用户，create
                if(dbUser.getHrmDepartment()!=null) {
                    wxCpUser=new WxCpUser();
                    wxCpUser.setUserId(dbUser.getLoginid());
                    wxCpUser.setDepartIds(new Long[]{dbUser.getHrmDepartment().getId()});
                    wxCpUser.setName(dbUser.getLastname());
                    wxCpUser.setEmail(dbUser.getEmail());
                    wxCpUser.setMobile(dbUser.getMobile());
                    wxCpUser.setEnable(1);
                    wxCpUser.setGender(dbUser.getSex().equals(0)?Gender.MALE:Gender.FEMALE);
                    userCreate.add(wxCpUser);
                }
            }
        }
        userCreate=userCreate.stream().filter(e->{
            if(e.getName().contains("次账号")){
                return false;
            }else{
                return  true;
            }
        }).collect(Collectors.toList());
        if(!userCreate.isEmpty()){
            Map<String,WxCpUser> map = Maps.newHashMap();
            for(WxCpUser user:userCreate){
                    try {
                        wxCpUserService.create(user);
                    }catch (WxErrorException e){
                        if(e.getError().getErrorCode()== Env.ERROR_CODE_MOBILE){
                            if(map.size()<1){
                                wxCpUserList.forEach(entity -> {
                                        map.put(entity.getUserId().trim(), entity);
                                });
                            }
                            //手机号码已存在
                            WxCpUser result = validateDepart(
                                    user.getUserId(),
                                    user.getDepartIds()[0]
                                    ,map);
                            if(result!=null){
                                  //手机号码已存在，执行修改操作
                                result.setDepartIds(user.getDepartIds());
                                try {
                                    wxCpUserService.update(result);
                                } catch (WxErrorException e1) {
                                    e1.printStackTrace();
                                    log.error("人员修改失败:",e1);
                                }
                            }else{
                                log.error("人员创建失败:",e);
                            }
                        }
                    }
            }
        }
    }

    //根据电话号码校验部门
    private WxCpUser validateDepart(String userId,Long depId,Map<String,WxCpUser> map){
        boolean flag=false;
        WxCpUser wxCpUser =map.get(userId);
       if(wxCpUser!=null){
          for(int i=0;i<wxCpUser.getDepartIds().length;i++ ){
             if( wxCpUser.getDepartIds()[i].equals(depId)){
                 flag = true;
                 break;
             }
          }
       }
      if(!flag){
           return wxCpUser;
      }
      return null;
    }

}
