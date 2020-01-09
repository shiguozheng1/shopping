package com.step.utils.net;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import com.step.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushubin  on 2019-08-29.
 * email:604580436@qq.com
 */
public enum RestClientFactory {
    INSTANCE;
    private RestClientCreator instance;
    RestClientFactory() {
        this.instance = new RestClientCreator();
    }

    public RestClientCreator getInstance() {
        return instance;
    }

    public class RestClientCreator{
        private CloseableHttpClient httpClient=null;
        public  String doPost(String url,Map<String,Object> params,Object object,Map<String,String> headers) {
            httpClient= HttpClientBuilder.create().build();
            String result = null;
          // 创建Post请求
            HttpPost httpPost = new HttpPost(url);
            if(headers==null) {
                httpPost.setHeader("Content-Type", "application/json;charset=utf8");
            }else{
                httpPost.setHeader("Content-Type", "application/json;charset=utf8");
                for(Map.Entry<String,String> entry:headers.entrySet()){
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            Object toJson = params == null ? object : params;
            String jsonString = JsonUtils.toString(toJson,true);
            StringEntity entity = new StringEntity(jsonString, "UTF-8");
            httpPost.setEntity(entity);


            // 响应模型
            CloseableHttpResponse response = null;
            try {
                // 由客户端执行(发送)Get请求
                response = httpClient.execute(httpPost);
                // 从响应模型中获取响应实体
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    result = EntityUtils.toString(responseEntity);
                    return result;
                }else{
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 释放资源
                    if (httpClient != null) {
                        httpClient.close();
                    }
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        public  String doDelete(String url,Map<String,String> headers,String params) {

            HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
            httpClient= HttpClientBuilder.create().build();
            String result = null;
            httpDelete.setHeader("Content-Type", "application/json;charset=utf8");
            for(Map.Entry<String,String> entry:headers.entrySet()){
                httpDelete.setHeader(entry.getKey(), entry.getValue());
            }
            StringEntity requestEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
                if (httpDelete != null) {
                    httpDelete.setEntity(requestEntity);
                }


            // 响应模型
            CloseableHttpResponse response = null;
            try {
                // 由客户端执行(发送)Get请求
                response = httpClient.execute(httpDelete);
                // 从响应模型中获取响应实体
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    result = EntityUtils.toString(responseEntity);
                    return result;
                }else{
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 释放资源
                    if (httpClient != null) {
                        httpClient.close();
                    }
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * 发送 get请求
         */
        public String doGet(String url,Map<String,Object> data,Map<String,String> headers) {
            httpClient = HttpClientBuilder.create().build();
            String result = null;
            // 创建参数队列
            try {
                List<NameValuePair> params;
                String requestUrl=url;
                if(!data.isEmpty()){
                    params = new ArrayList<NameValuePair>();
                    for(Map.Entry<String,Object> entry:data.entrySet()){
                        params.add(new BasicNameValuePair(entry.getKey(),String.valueOf(entry.getValue())));
                    }
                    String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(params, "UTF-8"));
                    requestUrl = url + "?" + paramsStr;
                }
                // 创建httpget.
                HttpGet httpget = new HttpGet(requestUrl);
                RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000)
                        .setSocketTimeout(5000).setConnectTimeout(5000).build();
                httpget.setConfig(requestConfig);
                httpget.setHeader("Content-Type", "application/json;charset=utf8");
                if(!headers.isEmpty()) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        httpget.setHeader(entry.getKey(), entry.getValue());
                    }
                }
                // 执行get请求.
                CloseableHttpResponse response = httpClient.execute(httpget);
                try {
                    // 获取响应实体
                    HttpEntity entity = response.getEntity();
                    // 打印响应状态
                    if (entity != null) {
                        result = EntityUtils.toString(entity);
                        return result;
                    }
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭连接,释放资源
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

    public  String doPost(String url,Map<String,Object> params){
        return instance.doPost(url,params,null,null);
    }


}
