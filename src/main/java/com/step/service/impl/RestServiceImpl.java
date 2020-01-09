package com.step.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.step.entity.dto.*;
import com.step.entity.dto.req.BaseRequestData;
import com.step.entity.dto.res.*;

import com.step.properties.MaycurProperties;
import com.step.service.RestService;
import com.step.utils.JsonUtils;
import com.step.utils.net.RestClientFactory;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 */
@Service
@Slf4j
public class RestServiceImpl implements RestService{
    @Autowired
    private MaycurProperties maycurProperties;

    @Override
    public ResponseResult<LoginInfoDto> requestLogin() {
        long  timestamp= Instant.now().toEpochMilli();
        Map<String,Object> params = new HashMap();
        params.put("appCode",maycurProperties.getAppCode());
        params.put("timestamp",timestamp);
        params.put("secret",maycurProperties.getSecret(timestamp));
       String resJson= RestClientFactory.INSTANCE.doPost(
                maycurProperties.getLoginUrl(),
                params
        );
        ResponseResult<LoginInfoDto>
                resultInfo =
                JsonUtils.toBean(resJson,
                        new TypeReference<ResponseResult<LoginInfoDto>>(){});
        return resultInfo;
    }




    /**
     * 通过时间戳获取每刻组织架构数据
     * @param tokenId
     * @param entCode
     * @param clazz
     * @return
     */
    @Override
    public <T> ResponsePostData<T> requestPostFindData(String tokenId, String entCode, String requestUrl, TypeReference<? extends BaseResponse> clazz) {
        Map<String,String> headers = new HashMap();
        headers.put("tokenId",tokenId);
        headers.put("entCode",entCode);
        Map<String,Object> params = new HashMap();
        params.put("start",null);
        params.put("end",null);

        String  resJson = RestClientFactory.INSTANCE.getInstance().
                doPost(requestUrl, params, null, headers);

        ResponsePostData<T> resultInfo = JsonUtils.toBean(resJson,clazz);

        if(resultInfo!=null&&!resultInfo.getCode().equals("ACK")){
            log.error("Post查询报错信息{}",resultInfo.getErrorData());
        }
        return resultInfo;
    }
    @Override
    public <T> ResponsePostData<T> requestSave(String tokenId, String entCode, BaseRequestData data, String requestUrl, TypeReference<? extends BaseResponse> clazz) {
        Assert.notNull(data);
        Map<String,String> headers = new HashMap();
        headers.put("tokenId",tokenId);
        headers.put("entCode",entCode);
        String resJson = RestClientFactory.INSTANCE.getInstance().
                doPost(requestUrl, null, data, headers);
        ResponsePostData<T> result = JsonUtils.toBean(resJson,clazz);
        if(result!=null&&!result.getCode().equals("ACK")){
            log.error("保存报错信息{}",result.getErrorData());
        }
        return result;
    }
    //Delete删除实体
    @Override
    public <T> ResponsePostData<T> requestDelete(String tokenId, String entCode, BaseRequestData data, String requestUrl, TypeReference<? extends BaseResponse> clazz) {
        Assert.notNull(data);
        Map<String,String> headers = new HashMap();
        headers.put("tokenId",tokenId);
        headers.put("entCode",entCode);
        String params=JsonUtils.toString(data);
        String resJson = RestClientFactory.INSTANCE.getInstance().
                doDelete(requestUrl, headers, params);
        ResponsePostData<T> result = JsonUtils.toBean(resJson,clazz);
        if(result!=null&&!result.getCode().equals("ACK")){
            log.error("删除报错信息:{}",result.getMessage());
        }
        return result;
    }

    /**
     * Get 请求查询信息
     * @return
     */
    @Override
    public <T>ResponseGetData<T> requestFindGetData(String tokenId, String entCode, String requestUrl,
                                                    Map<String,Object> requestData, TypeReference<ResponseGetData<T>> clazz){
        if(requestUrl==null){
            return null;
        }
        Map<String,String> headers = new HashMap();
        headers.put("tokenId",tokenId);
        headers.put("entCode",entCode);
        Map<String,Object> data=requestData;
        if(requestData==null){
            data= Maps.newHashMap();
            data.put("offset",String.valueOf(0));
            data.put("limit",String.valueOf(500));
        }
        String resJson = RestClientFactory.INSTANCE.getInstance().
                doGet(requestUrl,data,headers);
        ResponseGetData<T> result = JsonUtils.toBean(resJson,clazz);
        if(result!=null&&!result.getCode().equals("ACK")){
            log.error("Get查询报错信息{}",result.getErrorData());
        }
        return result;
    }


}
