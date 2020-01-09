package com.step.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.step.entity.dto.DifferEntity;
import com.step.entity.dto.res.ResponseGetData;
import com.step.entity.dto.res.ResponsePostData;
import com.step.entity.secondary.MaycurToken;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author: shiguozheng
 * @Date: 2019/8/28
 **/
public interface MaycurService {

    <T,K extends DifferEntity> List<K> formatGetData(String tokenId, String entCode, Function<T, K> function,TypeReference<ResponseGetData<T>> typeReference, List<K> sourceList, String url, Map<String,Object> requestData);

   <T,K extends DifferEntity,V> List<K> formatPostData(String tokenId, String entCode, Function<T, K > function, TypeReference<ResponsePostData<V>> typeReference, List<K> sourceList, String url);

    MaycurToken getToken();

    /***
     * 重新获取tokne
     * @param refreshTime 刷新时间
     * @return token
     */
    MaycurToken refreshToken(long refreshTime);
}
