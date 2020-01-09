package com.step.huilianyi.strategy;

import com.step.exception.OrgException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by zhushubin  on 2019-12-09.
 * email:604580436@qq.com
 */
@Slf4j
public  abstract  class Strategy  implements Comparable<Strategy>{
    /***
     * 排序
     */
    protected  Integer order;
    public abstract void invoke() throws OrgException;
    public int getOrder() {
        if(order==null){
            return 0;
        }
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int compareTo(Strategy o) {
        return this.getOrder() - o.getOrder();
    }

}
