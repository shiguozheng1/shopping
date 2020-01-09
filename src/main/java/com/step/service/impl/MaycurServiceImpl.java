package com.step.service.impl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.step.entity.dto.DifferEntity;;
import com.step.entity.dto.LoginInfoDto;
import com.step.entity.dto.res.ResponseGetData;
import com.step.entity.dto.res.ResponsePostData;
import com.step.entity.dto.res.ResponseResult;
import com.step.entity.secondary.MaycurToken;
import com.step.repository.secondary.MaycurTokenRepository;
import com.step.service.*;
import com.step.utils.DifferUtils;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: shiguozheng
 * @Date: 2019/8/28
 **/
@Service
@Slf4j
public class MaycurServiceImpl implements MaycurService {
    public static String LOGIN_TOKEN = "TOKEN_MAYCUR";
    @Autowired
    private RestService restService;

    @Autowired
    private MaycurTokenRepository maycurTokenRepository;

    @Override
    public <T,K extends DifferEntity,V> List<K> formatPostData(String tokenId, String entCode, Function<T, K> function, TypeReference<ResponsePostData<V>> typeReference, List<K> sourceList, String url) {
        ResponsePostData<V> result = restService.requestPostFindData(tokenId, entCode,url,typeReference);
        List<K> maycurList  =null;
        //有数据
        Assert.notNull(result);
        V maycurData=result.getData();
        if(maycurData!=null) {
            if (maycurData instanceof List) {
                if (maycurData != null && ((List) maycurData).size() > 0) {
                    maycurList = (List<K>) ((List) maycurData).stream().map(function).collect(Collectors.toList());
                }
            }
            List<K> ret = DifferUtils.diff(sourceList, maycurList);
            return ret;
        }
        return null;
    }


    @Override
    public <T,K extends DifferEntity> List<K> formatGetData(String tokenId, String entCode, Function<T, K> function,
                                                            TypeReference<ResponseGetData<T>> typeReference,
                                                            List<K> sourceList, String url,
                                                            Map<String,Object> requestData){


       List<K> maycurList=Lists.newArrayList();
        int offset = 0;
       if(requestData==null){
           requestData = Maps.newHashMap();
           requestData.put("offset",offset);
           requestData.put("limit",500);
       }else{
           requestData.put("offset",offset);
           requestData.put("limit",500);
       }
        do {
            ResponseGetData<T> result = restService.requestFindGetData(tokenId, entCode, url, requestData, typeReference);
            Assert.notNull(result);
            if(result.getData()!=null&&result.getData().size()>0){
                maycurList.addAll(result.getData().stream().map(function).collect(Collectors.toList()));
                offset+=result.getData().size();
                requestData.replace("offset",offset);
                if(result.getData().size()<500) {
                    //取数完成
                    break;
                }
            }

        }while (true);
        //比对差异
        List<K> ret = DifferUtils.diff(sourceList,maycurList);
        return ret;
    }

    /**
     * 获取tokenId
     */
    @Override
    @Transactional(value = "transactionManagerSecondary",readOnly = true)
    public MaycurToken getToken(){
        Optional<MaycurToken> ret= maycurTokenRepository.findById(LOGIN_TOKEN);
       if(ret.isPresent()){
           return ret.get();
       }
        return null;
    }

    /***
     * 刷新token
     * @param refreshTime 刷新时间
     * @return
     */
    @Override
    @Transactional("transactionManagerSecondary")
    public MaycurToken refreshToken(long refreshTime) {
        MaycurToken maycurToken=new MaycurToken();
        ResponseResult<LoginInfoDto> result=restService.requestLogin();
        Assert.notNull(result);
        log.error("每刻身份认证",result);
        Assert.isTrue(result.getSuccess(),"每刻身份认证失败");
        maycurToken.setId(LOGIN_TOKEN);
        maycurToken.setTokenId(result.getData().getTokenId());
        maycurToken.setEntCode(result.getData().getEntCode());
        maycurToken.setRefreshTime(refreshTime);
        maycurTokenRepository.save(maycurToken);
        return maycurToken;
    }

}
