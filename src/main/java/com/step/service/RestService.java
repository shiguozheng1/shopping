package com.step.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.step.entity.dto.LoginInfoDto;
import com.step.entity.dto.req.BaseRequestData;
import com.step.entity.dto.res.BaseResponse;
import com.step.entity.dto.res.ResponseGetData;
import com.step.entity.dto.res.ResponsePostData;
import com.step.entity.dto.res.ResponseResult;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 * 网路请求
 */
public interface RestService {
    //登录
    ResponseResult<LoginInfoDto> requestLogin();
    //查询每刻业务实体与部门
   <T> ResponsePostData<T> requestPostFindData(String tokenId, String entCode, String requestUrl, TypeReference<? extends BaseResponse> clazz);

    //批量保存与修改数据
    <T>ResponsePostData<T> requestSave(String tokenId, String entCode, BaseRequestData data, String requestUrl, TypeReference<? extends BaseResponse> clazz);

    //根据Code 批量删除
    <T>ResponsePostData<T> requestDelete(String tokenId, String entCode, BaseRequestData data,String requestUrl,TypeReference<? extends BaseResponse> clazz);

    //Get 查询数据
    <T>ResponseGetData<T> requestFindGetData(String tokenId, String entCode, String requestUrl, Map<String,Object> requestData,TypeReference<ResponseGetData<T>> clazz);

}
