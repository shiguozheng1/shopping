package com.step.config;

import com.google.common.collect.Maps;
import com.step.properties.WxCpProperties;
import lombok.val;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpInMemoryConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-06-27.
 * email:604580436@qq.com
 * 企业微信号配置
 */
@Configuration
@EnableConfigurationProperties(WxCpProperties.class)
public class WxCpConfiguration {
    private WxCpProperties properties;
    /***
     * agentId 对应的服务
     */
    private static Map<Integer, WxCpService> cpServices = Maps.newHashMap();

    /**
     * 构造函数注册实体类
     * @param properties 配置信息
     */
    @Autowired
    public WxCpConfiguration( WxCpProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void initServices() {
        cpServices = this.properties.getAppConfigs().stream().map(a -> {
            val configStorage = new WxCpInMemoryConfigStorage();
            configStorage.setCorpId(this.properties.getCorpId());
            configStorage.setAgentId(a.getAgentId());
            configStorage.setCorpSecret(a.getSecret());
            configStorage.setToken(a.getToken());
            configStorage.setAesKey(a.getAesKey());
            val service = new WxCpServiceImpl();
            service.setWxCpConfigStorage(configStorage);
            return service;
        }).collect(Collectors.toMap(service -> service.getWxCpConfigStorage().getAgentId(), a -> a));
    }
    public static WxCpService getCpService(Integer agentId) {
        return cpServices.get(agentId);
    }
}
