package com.step.adapter;

import me.chanjar.weixin.cp.bean.WxCpDepart;

/**
 * Created by zhushubin  on 2019-06-28.
 * email:604580436@qq.com
 */
public class WxCpDepartAdapter {
    private WxCpDepart wxCpDepart;
    public WxCpDepartAdapter(WxCpDepart wxCpDepart){
        this.wxCpDepart = wxCpDepart;
    }
    public String getTreePath(){
        StringBuilder builder = new StringBuilder();
        return null;
    }
}
