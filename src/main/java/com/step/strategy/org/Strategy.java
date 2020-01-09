package com.step.strategy.org;

import com.step.entity.secondary.MaycurToken;
import com.step.exception.OrgException;
import com.step.properties.MaycurProperties;
import com.step.service.HrmSubCompanyService;
import com.step.service.MaycurService;
import com.step.service.RestService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public  abstract  class  Strategy {
    @Autowired
    protected MaycurProperties maycurProperties;
    @Autowired
    protected MaycurService maycurService;
    @Autowired
    protected RestService restService;

    public Strategy(){};
    // 过期时间
    private int timeout = 30 * 60 * 1000; // 30分钟

    // 间隔时间
    private int interval =2 * 60 * 60 * 1000; // 4小时

    // 刷新时间。
    private static volatile long refreshTime = -1L;//getNextRefreshTime(System.currentTimeMillis(),this.interval);
    public  static volatile MaycurToken token = null;
    public void doSync() throws Exception{
         refreshToken();
         invoke();
    }

    public abstract void invoke() throws OrgException;
    /**
     * 设置认证过期时间。默认30分钟。
     * @param timeout
     * 单位分钟
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout * 60 * 1000;
    }

    /**
     * 设置刷新数据库时间。默认4小时。
     * @param interval
     * 单位分钟
     */
    public void setInterval(int interval) {
        this.interval = interval * 60 * 1000;
        this.refreshTime = getNextRefreshTime(System.currentTimeMillis(),
                this.interval);
    }

    /**
     * 获得下一个刷新时间。
     * @param current
     * @param interval
     * @return 随机间隔时间
     */
    private long getNextRefreshTime(long current, int interval) {
        return current + interval;
        // 为了防止多个应用同时刷新，间隔时间=interval+RandomUtils.nextInt(interval/4);
        // return current + interval + RandomUtils.nextInt(interval / 4);
    }
    public void refreshToken(){
        Long currentTime = new DateTime().getMillis();
        //从数据库获取token ,token 有效期
        //拿token的有效期来比对
        if(refreshTime<currentTime){
            refreshTime = getNextRefreshTime(currentTime,interval);
            //@TODO 已经过期重新获取token
            try {
                token = maycurService.refreshToken(refreshTime);
            }catch (IllegalArgumentException e){
                refreshTime = -1L;
                throw e;
            }catch (Exception e){
                refreshTime = -1L;
                throw new RuntimeException("sql exception at refresh maycur access_token");
            }
            log.info("refresh Authentication{}",refreshTime);
        }
    }

}
